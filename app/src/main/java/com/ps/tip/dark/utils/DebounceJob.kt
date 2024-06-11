package com.ps.tip.dark.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DebounceJob {
    private var job: Job? = null

    fun debounce(
        delayMs: Long = 500L,
        scope: CoroutineScope,
        func: () -> Unit,
    ): () -> Unit {
        job?.cancel()
        return {
            job = scope.launch {
                delay(delayMs)
                func()
            }
        }
    }
}