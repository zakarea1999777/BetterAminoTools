package com.better.amino.fragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.WorkerThread;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.better.amino.R;
import com.better.amino.activities.RequestActivity;
import com.better.amino.adapters.RequestsAdapter;
import com.better.amino.utils.FileUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DeveloperFragment extends Fragment {

    RequestsAdapter requestsAdapter;
    ExtendedFloatingActionButton floatingActionButton;
    RecyclerView project_recycler;
    SwipeRefreshLayout refreshLayout;
    LinearLayout empty_container;
    private OnRequestCreatedListener mListener;
    private BottomSheetDialog createNewProjectDialog;
    private BottomSheetDialog deleteProjectDialog;

    public void setOnProjectCreatedListener(OnRequestCreatedListener listener) {
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_developer, container, false);

        buildCreateNewProjectDialog();
        buildDeleteProjectDialog();

        floatingActionButton = view.findViewById(R.id.fab);
        project_recycler = view.findViewById(R.id.project_recycler);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        empty_container = view.findViewById(R.id.empty_container);

        requestsAdapter = new RequestsAdapter(requireContext(), new ArrayList<>());
        requestsAdapter.setOnRequestClickedListener(this::openProject);
        requestsAdapter.setOnRequestLongClickedListener(this::deleteProject);
        setOnProjectCreatedListener(this::openProject);

        project_recycler.setAdapter(requestsAdapter);
        project_recycler.setLayoutManager(new LinearLayoutManager(requireActivity()));

        floatingActionButton.setOnClickListener(vie -> showCreateNewProjectDialog());

        refreshLayout.setOnRefreshListener(
                () -> {
                    loadProjects();
                    refreshLayout.setRefreshing(false);
                });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        requestsAdapter = new RequestsAdapter(requireContext(), new ArrayList<>());
        requestsAdapter.setOnRequestClickedListener(this::openProject);
        requestsAdapter.setOnRequestLongClickedListener(this::deleteProject);
        setOnProjectCreatedListener(this::openProject);

        project_recycler.setAdapter(requestsAdapter);
        project_recycler.setLayoutManager(new LinearLayoutManager(requireActivity()));
        loadProjects();
    }

    @Override
    public void onDestroy() {
        if (createNewProjectDialog.isShowing()) {
            createNewProjectDialog.dismiss();
        }
        if (deleteProjectDialog.isShowing()) {
            deleteProjectDialog.dismiss();
        }
        super.onDestroy();
    }

    private void buildCreateNewProjectDialog() {
        createNewProjectDialog = new BottomSheetDialog(requireContext());
        createNewProjectDialog.setContentView(R.layout.create_new_project_dialog);
    }

    private void buildDeleteProjectDialog() {
        deleteProjectDialog = new BottomSheetDialog(requireContext());
        deleteProjectDialog.setContentView(R.layout.delete_dialog);
    }

    @WorkerThread
    private void showCreateNewProjectDialog() {
        if (!createNewProjectDialog.isShowing()) {
            createNewProjectDialog.show();
            EditText input = createNewProjectDialog.findViewById(android.R.id.text1);
            Button cancelBtn = createNewProjectDialog.findViewById(android.R.id.button2);
            Button createBtn = createNewProjectDialog.findViewById(android.R.id.button1);

            cancelBtn.setOnClickListener(v -> createNewProjectDialog.dismiss());
            createBtn.setOnClickListener(
                    v -> {
                        ArrayList<Map<String, Object>> lst = new ArrayList<>();
                        if (FileUtils.isFilePresent(requireContext(), "requests.json")) {
                            lst = new Gson().fromJson(FileUtils.read(requireContext(), "requests.json"), new TypeToken<ArrayList<Map<String, Object>>>() {
                            }.getType());
                        }
                        Map<String, Object> map = new HashMap<>();
                        map.put("name", input.getText().toString());
                        map.put("dataType", "JSON String");
                        map.put("json", "");
                        ArrayList<Map<String, Object>> dataList = new ArrayList<>();
                        map.put("data", dataList);
                        map.put("method", "POST");
                        map.put("url", "/<ndcId>/s/community/join?<uid>");
                        map.put("response", "");
                        lst.add(map);
                        requestsAdapter.add(map);
                        FileUtils.create(requireContext(), "requests.json", new Gson().toJson(lst));
                        mListener.onProjectCreated(lst.size() - 1);
                        createNewProjectDialog.dismiss();
                    });

            input.setText("");
        }
    }

    private void showDeleteProjectDialog(Map<String, Object> project, int position) {
        if (!deleteProjectDialog.isShowing()) {
            deleteProjectDialog.show();
            TextView title = deleteProjectDialog.findViewById(android.R.id.title);
            TextView message = deleteProjectDialog.findViewById(android.R.id.message);
            Button cancelBtn = deleteProjectDialog.findViewById(android.R.id.button2);
            Button deleteBtn = deleteProjectDialog.findViewById(android.R.id.button1);
            cancelBtn.setOnClickListener(v -> deleteProjectDialog.dismiss());
            title.setText(getString(R.string.delete_project));
            message.setText(getString(R.string.project_delete_warning, project.get("name")));
            deleteBtn.setOnClickListener(v -> {
                requestsAdapter.delete(position);
                ArrayList<Map<String, Object>> projects = new Gson().fromJson(FileUtils.read(requireContext(), "requests.json"), new TypeToken<ArrayList<HashMap<String, Object>>>() {
                }.getType());
                projects.remove(position);
                FileUtils.create(requireContext(), "requests.json", new Gson().toJson(projects));
                loadProjects();
                deleteProjectDialog.dismiss();
            });
        }
    }

    private void openProject(int position) {
        Intent intent = new Intent(requireContext(), RequestActivity.class);
        intent.putExtra("name", requestsAdapter.get(position).get("name").toString());
        startActivity(intent);
    }

    private void deleteProject(int position) {
        showDeleteProjectDialog(requestsAdapter.get(position), position);
    }

    private void loadProjects() {
        ArrayList<Map<String, Object>> projects = new Gson().fromJson(FileUtils.read(requireContext(), "requests.json"), new TypeToken<ArrayList<HashMap<String, Object>>>() {
        }.getType());

        if (projects != null) {
            for (Map<String, Object> project : projects) {
                requestsAdapter.add(project);
            }
        }
        toggleNullProject(projects);
    }

    private void toggleNullProject(ArrayList<Map<String, Object>> projects) {
        if (projects == null) {
            project_recycler.setVisibility(View.GONE);
            empty_container.setVisibility(View.VISIBLE);
        } else {
            if (projects.size() == 0) {
                project_recycler.setVisibility(View.GONE);
                empty_container.setVisibility(View.VISIBLE);
            } else {
                project_recycler.setVisibility(View.VISIBLE);
                empty_container.setVisibility(View.GONE);
            }
        }
    }

    public interface OnRequestCreatedListener {
        void onProjectCreated(int position);
    }
}