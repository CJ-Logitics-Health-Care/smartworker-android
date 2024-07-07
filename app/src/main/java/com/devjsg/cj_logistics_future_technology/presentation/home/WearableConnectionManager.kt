package com.devjsg.cj_logistics_future_technology.presentation.home

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WearableConnectionManager @Inject constructor(@ApplicationContext private val context: Context) {

    private val nodeClient = Wearable.getNodeClient(context)

    fun getConnectedNodes(): Task<List<Node>> {
        return nodeClient.connectedNodes
    }

    fun isAnyDeviceConnected(): Task<Boolean> {
        return getConnectedNodes().continueWith { task ->
            val nodes = task.result
            nodes != null && nodes.isNotEmpty()
        }
    }

    fun printConnectedNodes() {
        getConnectedNodes().addOnSuccessListener { nodes ->
            for (node in nodes) {
                Log.d(TAG, "Connected Node: ${node.displayName}, ${node.id}")
            }
        }
    }

    companion object {
        private const val TAG = "WearableConnectionManager"
    }
}