package com.devjsg.cj_logistics_future_technology.presentation.home

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.NodeClient
import com.google.android.gms.wearable.Wearable
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WearableConnectionManager @Inject constructor(@ApplicationContext private val context: Context) {

    private val nodeClient: NodeClient = Wearable.getNodeClient(context)

    suspend fun getConnectedNodes(): List<Node> = withContext(Dispatchers.IO) {
        return@withContext try {
            Tasks.await(nodeClient.connectedNodes)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get connected nodes", e)
            emptyList<Node>()
        }
    }

    suspend fun getWearableNodeId(): String? = withContext(Dispatchers.IO) {
        val nodes = getConnectedNodes()
        return@withContext nodes.firstOrNull()?.id
    }

    companion object {
        private const val TAG = "WearableConnectionMgr"
    }
}