package aplicacionmovil.isavanzados.com.mx.newrealm

import android.app.AlertDialog
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.view.*
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import aplicacionmovil.isavanzados.com.mx.newrealm.Adapters.NoteAdapter
import aplicacionmovil.isavanzados.com.mx.newrealm.Models.Board
import aplicacionmovil.isavanzados.com.mx.newrealm.Models.Note
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmList

class NoteActivity : AppCompatActivity(), RealmChangeListener<Board>{

    var listView: ListView? = null

    var addNote: FloatingActionButton? = null

    var adapter: NoteAdapter? = null
    var notes: RealmList<Note>? = null

    var realm: Realm? = null

    var boardId: Int? = null
    var board: Board? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        realm = Realm.getDefaultInstance()

        if(intent.extras != null){

            boardId = intent.extras.getInt("id")

        }

        board = realm!!.where(Board::class.java).equalTo("id",boardId).findFirst()
        board!!.addChangeListener(this)
        notes = board!!.notes

        this.title = board!!.title

        addNote = findViewById<FloatingActionButton>(R.id.fabAddNote) as FloatingActionButton
        listView = findViewById<ListView>(R.id.listViewNote) as ListView

        adapter = NoteAdapter(this@NoteActivity, notes, R.layout.list_view_note_item)

        listView!!.adapter = adapter;

        addNote!!.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                showAlertForCreatingNote("Add New Note","Type a note for " + board!!.title + ".")
            }
        })

        registerForContextMenu(listView)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_note, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item!!.itemId){

            R.id.delete_all_note -> {

                deleteAll()

                return true

            }

            else -> {
                return super.onOptionsItemSelected(item)
            }

        }

    }

    fun deleteAll(){
        realm!!.beginTransaction()
        board!!.notes!!.deleteAllFromRealm()
        realm!!.commitTransaction()
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        menuInflater.inflate(R.menu.context_menu_note, menu)
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {

        var info: AdapterView.AdapterContextMenuInfo = item!!.menuInfo as AdapterView.AdapterContextMenuInfo

        when(item!!.itemId){

            R.id.edit_note -> {
                showAlertForEditingNote("Edit Note", "Change the name of the note",notes!!.get(info.position)!!)
                return true
            }

            R.id.delete_note-> {
                deleteNote(notes!!.get(info.position)!!)
                return true
            }

            else -> {
                return super.onContextItemSelected(item)
            }

        }
    }

    fun showAlertForCreatingNote(title: String, message: String) {

        var builder: AlertDialog.Builder = AlertDialog.Builder(this@NoteActivity)

        if (title != null) builder.setTitle(title)

        if (message != null) builder.setMessage(message)

        var viewInflated: View = LayoutInflater.from(this@NoteActivity).inflate(R.layout.dialog_create_note, null)

        builder.setView(viewInflated)

        var input: EditText = viewInflated.findViewById<EditText>(R.id.editTextNewNote) as EditText

        builder.setPositiveButton("Add", DialogInterface.OnClickListener { dialog, which ->

            var note = input.text.toString().trim()

            if (note.length > 0) {
                creteNewNote(note)
            } else {
                Toast.makeText(this@NoteActivity, "The note can't be empty.", Toast.LENGTH_LONG).show()
            }

        })
        var dialog: AlertDialog = builder.create()
        dialog.show()

    }

    fun creteNewNote(note: String){

        realm!!.beginTransaction()
        var _note = Note(note)
        realm!!.copyToRealm(_note)
        board!!.notes!!.add(_note)
        realm!!.commitTransaction()

    }

    fun showAlertForEditingNote(title: String, message: String, note: Note) {

        var builder: AlertDialog.Builder = AlertDialog.Builder(this@NoteActivity)

        if (title != null) builder.setTitle(title)

        if (message != null) builder.setMessage(message)

        var viewInflated: View = LayoutInflater.from(this@NoteActivity).inflate(R.layout.dialog_create_note, null)

        builder.setView(viewInflated)

        var input: EditText = viewInflated.findViewById<EditText>(R.id.editTextNewNote) as EditText
        input.setText(note.description.toString())

        builder.setPositiveButton("Add", DialogInterface.OnClickListener { dialog, which ->

            var noteDescription = input.text.toString().trim()

            if(noteDescription.length == 0){
                Toast.makeText(this@NoteActivity, "The text for the note is qequired to be edited.", Toast.LENGTH_LONG).show()
            }else if(noteDescription.equals(note.description)){
                Toast.makeText(this@NoteActivity, "The note is the same than it was before.", Toast.LENGTH_LONG).show()
            }else{
                editNote(noteDescription, note)
            }


        })
        var dialog: AlertDialog = builder.create()
        dialog.show()

    }

    fun editNote(newNoteDescription: String, note: Note){
        realm!!.beginTransaction()
        note.description = newNoteDescription
        realm!!.copyToRealmOrUpdate(note)
        realm!!.commitTransaction()
    }

    fun deleteNote(note: Note){
        realm!!.beginTransaction()
        note.deleteFromRealm()
        realm!!.commitTransaction()
    }

    override fun onChange(t: Board?) {
        adapter!!.notifyDataSetChanged()
    }

}
