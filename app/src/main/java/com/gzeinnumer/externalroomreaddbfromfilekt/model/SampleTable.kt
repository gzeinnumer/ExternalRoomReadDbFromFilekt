package com.gzeinnumer.externalroomreaddbfromfilekt.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sample_table")
class SampleTable(
    @field:PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String
)