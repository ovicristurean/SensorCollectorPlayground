package com.ovidiucristurean.domain

import com.ovidiucristurean.domain.model.RotationModel

interface RotationEventListener {
    fun onRotationChanged(rotationModel: RotationModel)
}