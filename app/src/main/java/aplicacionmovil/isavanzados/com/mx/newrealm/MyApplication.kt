package aplicacionmovil.isavanzados.com.mx.newrealm

import android.app.Application
import aplicacionmovil.isavanzados.com.mx.newrealm.Models.Board
import aplicacionmovil.isavanzados.com.mx.newrealm.Models.Note
import com.facebook.stetho.Stetho
import com.uphyca.stetho_realm.RealmInspectorModulesProvider
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmObject
import java.util.concurrent.atomic.AtomicInteger


class MyApplication : Application(){

    companion object {
        var BoardID = AtomicInteger()
        var NoteID = AtomicInteger()
    }


    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        val config = RealmConfiguration.Builder().name("myrealm.realm").deleteRealmIfMigrationNeeded().build()
        Realm.setDefaultConfiguration(config)

        val realm = Realm.getDefaultInstance()
        BoardID = getIdByTable(realm, Board::class.java)
        NoteID = getIdByTable(realm, Note::class.java)

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());

        realm.close()

    }

    fun <T : RealmObject> getIdByTable(realm: Realm, anyClass: Class<T>): AtomicInteger {
        val results = realm.where(anyClass).findAll()
        return if (results.size > 0) AtomicInteger(results.max("id")!!.toInt()) else AtomicInteger()
    }

}