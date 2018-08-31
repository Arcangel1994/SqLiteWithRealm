package aplicacionmovil.isavanzados.com.mx.newrealm.Models

import aplicacionmovil.isavanzados.com.mx.newrealm.MyApplication
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

open class Note() : RealmObject() {

    @PrimaryKey
    var id: Int? = null

    @Required
    var description: String? = null

    @Required
    var createdAt: Date? = null

    constructor(description: String): this() {
        this.id = MyApplication.NoteID.incrementAndGet()
        this.description = description
        this.createdAt = Date()
    }


}