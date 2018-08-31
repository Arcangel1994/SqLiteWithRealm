package aplicacionmovil.isavanzados.com.mx.newrealm.Models

import aplicacionmovil.isavanzados.com.mx.newrealm.MyApplication
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

open class Board() : RealmObject() {

    @PrimaryKey
    var id: Int? = null

    @Required
    var title: String? = null

    @Required
    var createdAt: Date? = null

    var notes: RealmList<Note>? = null

    constructor(title: String?) : this() {
        this.id = MyApplication.NoteID.incrementAndGet()
        this.title = title
        this.createdAt = Date()
        this.notes = RealmList<Note>()
    }

}