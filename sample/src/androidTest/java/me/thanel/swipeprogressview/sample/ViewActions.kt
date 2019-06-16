package me.thanel.swipeprogressview.sample

import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.CoordinatesProvider
import androidx.test.espresso.action.GeneralSwipeAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Swipe

internal fun swipe(
    startCoordinates: CoordinatesProvider,
    endCoordinates: CoordinatesProvider
): ViewAction = GeneralSwipeAction(Swipe.FAST, startCoordinates, endCoordinates, Press.FINGER)
