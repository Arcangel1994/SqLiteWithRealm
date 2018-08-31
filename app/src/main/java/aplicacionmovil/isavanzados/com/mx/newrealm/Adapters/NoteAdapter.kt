package aplicacionmovil.isavanzados.com.mx.newrealm.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import aplicacionmovil.isavanzados.com.mx.newrealm.Models.Note
import aplicacionmovil.isavanzados.com.mx.newrealm.R
import java.text.DateFormat
import java.text.SimpleDateFormat

class NoteAdapter() : BaseAdapter() {

    var context: Context? = null
    var list: List<Note>? = null
    var layout: Int? = null

    constructor(context: Context?, list: List<Note>?, layout: Int?): this(){
        this.context = context
        this.list = list
        this.layout = layout
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var convertView = convertView;
        var ViewH: ViewHolder? = null

        if(convertView == null){

            convertView = LayoutInflater.from(context).inflate(layout!!, null)
            ViewH = ViewHolder()

                ViewH.NoteDescription = convertView.findViewById<TextView>(R.id.textViewNoteDescription) as TextView
                ViewH.NoteCreatedAt = convertView.findViewById<TextView>(R.id.textViewNoteCreatedAt) as TextView

            convertView.setTag(ViewH)

        }else{

            ViewH = convertView.getTag() as ViewHolder

        }

        var note: Note = list!!.get(position)


        ViewH.NoteDescription!!.text = note.description

        var df: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        var createdAt: String = df.format(note.createdAt)

        ViewH.NoteCreatedAt!!.text = createdAt.toString()

        return convertView!!

    }

    override fun getItem(position: Int): Any {

        return list!!.get(position)

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list!!.size
    }

    class ViewHolder{

        var NoteDescription: TextView? = null
        var NoteCreatedAt: TextView? = null

    }

}