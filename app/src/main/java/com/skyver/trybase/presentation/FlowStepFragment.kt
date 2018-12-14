package com.skyver.trybase.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.AuthResult
import com.skyver.trybase.R
import com.skyver.trybase.presentation.platform.BaseFragment
import com.skyver.trybase.presentation.entity.RepoView
import com.skyver.trybase.presentation.extention.*
import kotlinx.android.synthetic.main.flow_step_one_fragment.*
import javax.inject.Inject


class FlowStepFragment : BaseFragment() {

    @Inject
    lateinit var repoesAdapter: RepoesAdapter

    private lateinit var repoesViewModel: RepoesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        repoesViewModel = viewModel(viewModelFactory) {
            observe(repoes, ::renderMoviesList)
            failure(failure, ::handleFailure)

            //todo test
            observe(loginResult, ::renderLoginList)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        //val flowStepNumber = arguments?.getInt("flowStepNumber")

        // TODO Use type-safe arguments - remove previous line!
        val safeArgs = FlowStepFragmentArgs.fromBundle(arguments)
        val flowStepNumber = safeArgs.flowStepNumber


        return when (flowStepNumber) {
            2 -> inflater.inflate(R.layout.flow_step_two_fragment, container, false)
            else -> inflater.inflate(R.layout.flow_step_one_fragment, container, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeView()
        loadMoviesList()

        view.findViewById<View>(R.id.next_button).setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.next_action)
        )

    }

    private fun initializeView() {
        repoList.layoutManager = LinearLayoutManager(activity)
        repoList.adapter = repoesAdapter
        repoesAdapter.clickListener = { movie -> notify(movie.name) }
    }

    private fun loadMoviesList() {
        showProgress()
        repoesViewModel.loadRepoes()
    }

    private fun renderMoviesList(movies: List<RepoView>?) {
        repoesAdapter.collection = movies.orEmpty()
        hideProgress()

        //todo test
        repoesViewModel.craeteUser("me2BOOLEAN@hg.com", "free123445")
    }

    ////todo test
    private fun renderLoginList(authResult: Boolean?) {
        notify("LOGED IN!! $authResult")

    }

    private fun handleFailure(failure: Failure?) {
        hideProgress()

        when (failure) {
            is Failure.NetworkConnection -> renderFailure(R.string.failure_network_connection)
            is Failure.ServerError -> {
                renderFailure(failure.cause ?: fromResource(R.string.failure_unknown_error))
            }

            is Failure.OtherError -> {
                renderFailure(failure.cause ?: fromResource(R.string.failure_unknown_error))
            }
        }
    }

    private fun renderFailure(@StringRes message: Int) {
        notifyWithAction(message, R.string.action_refresh, ::loadMoviesList)
    }

    private fun renderFailure(message: String) {
        notifyWithAction(message, R.string.action_refresh, ::loadMoviesList)
    }
}


