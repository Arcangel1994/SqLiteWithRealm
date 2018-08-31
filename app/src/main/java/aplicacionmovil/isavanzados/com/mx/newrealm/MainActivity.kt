package aplicacionmovil.isavanzados.com.mx.newrealm

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.view.*
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import aplicacionmovil.isavanzados.com.mx.newrealm.Adapters.BoardAdapter
import aplicacionmovil.isavanzados.com.mx.newrealm.Models.Board
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults

class MainActivity : AppCompatActivity() , RealmChangeListener<RealmResults<Board>>, AdapterView.OnItemClickListener {

    var realm: Realm? = null

    var fab: FloatingActionButton? = null

    var listView: ListView? = null
    var adapter: BoardAdapter? = null
    var boards: RealmResults<Board>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //DB Realm
        realm = Realm.getDefaultInstance();

        boards = realm!!.where(Board::class.java).findAll()

        boards!!.addChangeListener(this)

        listView = findViewById<ListView>(R.id.listViewBoard) as ListView

        adapter = BoardAdapter(this@MainActivity, R.layout.list_view_board_item, boards)

        listView!!.adapter = adapter

        listView!!.setOnItemClickListener(this)

        fab = findViewById<FloatingActionButton>(R.id.fabAddBoard) as FloatingActionButton

        fab!!.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                showAlertForCreatingBoard("Add New Board", "Type a name for your new board")
            }
        })

        /*
            realm.beginTransaction();
            realm.deleteAll();
            realm,commitTreansaction();
         */

        registerForContextMenu(listView)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item!!.itemId){

            R.menu.menu -> {
                    realm!!.beginTransaction()
                    realm!!.deleteAll()
                    realm!!.commitTransaction()
                return true
            }

            else -> {
                return super.onOptionsItemSelected(item)
            }

        }

    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        var info : AdapterView.AdapterContextMenuInfo = menuInfo as AdapterView.AdapterContextMenuInfo
        menu!!.setHeaderTitle(boards!!.get(info.position)!!.title)
        menuInflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {

        var info : AdapterView.AdapterContextMenuInfo = item!!.menuInfo as AdapterView.AdapterContextMenuInfo

        when(item!!.itemId){

            R.id.delete_board -> {

                    deleteBoard(boards!!.get(info.position)!!)

                return true
            }

            R.id.edit_board ->{

                showAlertForEditingBoard("Edit Board","Change the name of the board", boards!!.get(info.position)!!)

                return true
            }

            else -> {
                return super.onContextItemSelected(item)
            }

        }
    }

    fun deleteBoard(board: Board){
        realm!!.beginTransaction()
        board.deleteFromRealm()
        realm!!.commitTransaction()
    }

    fun showAlertForEditingBoard(title: String, message: String, board: Board) {

        var builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)

        if (title != null) builder.setTitle(title)

        if (message != null) builder.setMessage(message)

        var viewInflated: View = LayoutInflater.from(this@MainActivity).inflate(R.layout.dialog_create_board, null)

        builder.setView(viewInflated)

        var input: EditText = viewInflated.findViewById<EditText>(R.id.editTextNewBoard) as EditText

        input.setText(board.title.toString())

        builder.setPositiveButton("Save", DialogInterface.OnClickListener { dialog, which ->

            var boardName = input.text.toString().trim()

            if(boardName.length == 0){
                Toast.makeText(this@MainActivity, "The name is required to edit the current Board.", Toast.LENGTH_LONG).show()
            }else if(boardName.equals(board.title)){
                Toast.makeText(this@MainActivity, "The name is same that it was before.", Toast.LENGTH_LONG).show()
            }else{

                editBoard(boardName, board)

            }
        })
        var dialog: AlertDialog = builder.create()
        dialog.show()

    }

    fun editBoard(newName: String, board: Board){
        realm!!.beginTransaction()
        board.title = newName
        realm!!.copyToRealmOrUpdate(board)
        realm!!.commitTransaction()
    }

    fun showAlertForCreatingBoard(title: String, message: String) {

        var builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)

        if (title != null) builder.setTitle(title)

        if (message != null) builder.setMessage(message)

        var viewInflated: View = LayoutInflater.from(this@MainActivity).inflate(R.layout.dialog_create_board, null)

        builder.setView(viewInflated)

        var input: EditText = viewInflated.findViewById<EditText>(R.id.editTextNewBoard) as EditText

        builder.setPositiveButton("Add", DialogInterface.OnClickListener { dialog, which ->

            var boardName = input.text.toString().trim()

            if (boardName.length > 0) {
                creteNewBoard(boardName)
            } else {
                Toast.makeText(this@MainActivity, "The name is required to create a new Board.", Toast.LENGTH_LONG).show()
            }

        })
        var dialog: AlertDialog = builder.create()
        dialog.show()

    }

    fun creteNewBoard(boardName: String) {

//        var boardName: String = boardName;

        realm!!.beginTransaction()
        var board: Board = Board(boardName.toString())
        realm!!.copyToRealm(board)
        realm!!.commitTransaction()

//        realm!!.executeTransaction(object : Realm.Transaction{
//            override fun execute(realm: Realm?) {
//
//                var board: Board = Board(boardName.toString())
//                realm!!.copyToRealm(board)
//
//            }
//        })

    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var intent: Intent = Intent(this@MainActivity, NoteActivity::class.java)
        intent.putExtra("id", boards!!.get(position)!!.id)
        startActivity(intent)
    }

    override fun onChange(t: RealmResults<Board>?) {
        adapter!!.notifyDataSetChanged()
    }

}
