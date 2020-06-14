/*
 *
 *  * Copyright 2015-2019 Amazon.com, Inc. or its affiliates.  All Rights Reserved.
 *
 */

package com.example.exchangeratesapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.ClassRule

open class DefaultSynchronousTest {

    companion object {
        /**
         * Custom rule to run RxJava tasks synchronously.
         */
        @ClassRule
        @JvmField
        val rxImmediateSchedulerRule = RxImmediateSchedulerRule()

        /**
         * A JUnit Test Rule that swaps the background executor used by the Architecture Components with a
         * different one which executes each task synchronously.
         */
        @ClassRule
        @JvmField
        val instantTaskExecutorRule = InstantTaskExecutorRule()
    }
}