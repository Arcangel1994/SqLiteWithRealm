package aplicacionmovil.isavanzados.com.mx.newrealm.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import aplicacionmovil.isavanzados.com.mx.newrealm.Models.Board
import aplicacionmovil.isavanzados.com.mx.newrealm.R
import java.text.DateFormat
import java.text.SimpleDateFormat

class BoardAdapter() : BaseAdapter() {

    var context: Context? = null
    var layout: Int? = null
    var list: List<Board>? = null

    constructor(context: Context?, layout: Int?, list: List<Board>?) : this() {
        this.context = context
        this.layout = layout
        this.list = list
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var convertView = convertView

        var holder: ViewHolder? = null

        if(convertView == null){

            val layoutInflater = LayoutInflater.from(context)
            convertView = layoutInflater.inflate(layout!!, null)

            holder = ViewHolder()

            holder.Title = convertView.findViewById<TextView>(R.id.textViewBoardTitle) as TextView
            holder.Notes = convertView.findViewById<TextView>(R.id.textViewBoardNotes) as TextView
            holder.CreateAt = convertView.findViewById<TextView>(R.id.textViewBoardDate) as TextView

            convertView.setTag(holder)


        }else{

            holder = convertView.getTag() as ViewHolder

        }

        var board: Board = list!!.get(position)

        holder.Title!!.text = board.title.toString()

        var numberOfNotes: Int = board!!.notes?.size!!

        val textForNotes = if (numberOfNotes == 1) numberOfNotes.toString() + " Note" else numberOfNotes.toString() + " Notes"

        holder.Notes!!.text = textForNotes.toString()

        var df: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        var createAt: String = df.format(board.createdAt)

        holder.CreateAt!!.text = createAt.toString()


        return convertView!!;

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

        var Title: TextView? = null
        var Notes: TextView? = null
        var CreateAt: TextView? = null

    }


}