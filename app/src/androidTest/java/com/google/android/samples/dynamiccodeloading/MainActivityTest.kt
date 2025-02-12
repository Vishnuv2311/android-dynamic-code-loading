/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.google.android.samples.dynamiccodeloading

import android.preference.PreferenceManager
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@LargeTest
class MainActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java, false, false)


    /**
     * Clear sharedpreferences before each run
     */
    @Before
    fun before() {
        PreferenceManager.getDefaultSharedPreferences(
            InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        ).edit().clear().commit()
    }

    /**
     * This is an (overly) simplified test that can be used locally with all APK splits installed.
     * It doesn't test Play Core APIs (on-demand delivery) since this is impossible without going through the Play Store
     */
    @Test
    fun mainActivityTest() {
        mActivityTestRule.launchActivity(null)
        val appCompatButton = onView(withId(R.id.incrementButton))
        appCompatButton.perform(click())
        appCompatButton.perform(click())
        appCompatButton.perform(click())

        onView(withId(R.id.saveButton)).perform(click())

        InstrumentationRegistry.getInstrumentation().runOnMainSync({ mActivityTestRule.activity.recreate() })

        val textView = onView(withId(R.id.counterText))
        textView.check(matches(withText("3")))
    }
}
