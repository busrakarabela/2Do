package com.example.a2do.model

import java.util.*

class Note (

    var id:String,
    var type_id:String,
    var baslik:String,
    var note:String,
    var isDeleted:Boolean=false,
    var alarmTime: Calendar?

)