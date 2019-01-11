package com.skyver.trybase.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.skyver.trybase.R
import com.skyver.trybase.presentation.platform.BaseFragment
import com.skyver.trybase.presentation.entity.RepoView
import com.skyver.trybase.presentation.entity.UserEntity
import com.skyver.trybase.presentation.extention.*
import kotlinx.android.synthetic.main.flow_step_one_fragment.*
import timber.log.Timber.d
import timber.log.Timber.e
import javax.inject.Inject


class FlowStepFragment : BaseFragment() {

    @Inject
    lateinit var repoesAdapter: RepoesAdapter

    private lateinit var repoesViewModel: RepoesViewModel

    //todo share feature
    private  var modelShare: SharedViewMode? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        repoesViewModel = viewModel(viewModelFactory) {
            observe(repoes, ::renderReposList)
            observe(allUsers, ::renderAllUsersList)
            failure(failure, ::handleFailure)
        }

        //todo share feature
        modelShare = sharedViewModel(viewModelFactory){
            observe(shared, ::updateUI)
        }

    }

    private fun updateUI(str: String?){
        text.text = str ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        //val flowStepNumber = arguments?.getInt("flowStepNumber")//alternative variant

        // TODO Use type-safe arguments - remove previous line!
//        val safeArgs = FlowStepFragmentArgs.fromBundle(arguments)//older version - current arguments not null
//        val flowStepNumber = safeArgs.flowStepNumber
        val safeArgs =
            if (arguments != null) FlowStepFragmentArgs.fromBundle(arguments!!)
            else null
        val flowStepNumber = safeArgs?.flowStepNumber

//        return when (flowStepNumber) {
//            2 -> inflater.inflate(R.layout.flow_step_two_fragment, container, false)
//            else -> inflater.inflate(R.layout.flow_step_one_fragment, container, false)
//        }
        return  inflater.inflate(R.layout.flow_step_one_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeView()
        loadReposList()


        view.findViewById<View>(R.id.next_button).setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.next_action)
        )

        view.findViewById<View>(R.id.button2).setOnClickListener{
            loadUsersList()
        }
    }

    private fun initializeView() {
        repoList.layoutManager = LinearLayoutManager(activity)
        repoList.adapter = repoesAdapter
        repoesAdapter.clickListener = { movie -> notify(movie.name) }
    }

    private fun loadUsersList() {
        showProgress()
        repoesViewModel.loadAllUsers()
    }

    private fun loadReposList() {
        showProgress()
        repoesViewModel.loadRepoes()
    }

    private fun renderReposList(movies: List<RepoView>?) {
        repoesAdapter.collection = movies.orEmpty()
        hideProgress()
    }

    private fun renderAllUsersList(list: List<UserEntity>?) {
        list?.let {
            var r = ""
            it.map{user ->  r += user.email +"\n"}
            d("AllUsers: $r")
        }
        hideProgress()
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
        notifyWithAction(message, R.string.action_refresh, ::loadReposList)
    }

    private fun renderFailure(message: String) {
        notifyWithAction(message, R.string.action_refresh, ::loadReposList)
    }
}


