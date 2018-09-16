package com.example.simpledemo.view.users;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.simpledemo.MainApplication;
import com.example.simpledemo.R;
import com.example.simpledemo.model.pojo.domain.User;
import com.example.simpledemo.view.userDetails.UserDetailsActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentUsers extends Fragment implements UsersListContract.View,
        UsersAdapter.OnUserClickedListener {

    @BindView(R.id.recycler_view) protected RecyclerView usersList;
    protected UsersListContract.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, rootView);
        initRecyclerView();
        presenter = MainApplication.getInstance().graph().getUserListPresenter();
        presenter.setView(this);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.viewShown();
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        usersList.setLayoutManager(manager);
    }

    @Override
    public void showUsers(List<User> users) {
        UsersAdapter adapter = (UsersAdapter) usersList.getAdapter();
        if (adapter == null) {
            adapter = new UsersAdapter();
            adapter.setOnUserClickedListener(this);
            usersList.setAdapter(adapter);
        }

        adapter.replaceItems(users);
    }

    @Override
    public void onUserClicked(User user) {
        Intent intent = new Intent(getContext(), UserDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(UserDetailsActivity.EXTRA_USER_ID, user.getId());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void showError(String error) {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.error_title)
                .setMessage(error)
                .setPositiveButton(R.string.ok, null)
                .create()
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.viewDestroyed();
    }
}
