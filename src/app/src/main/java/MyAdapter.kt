import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.followme.R
import kotlinx.android.synthetic.main.roboter_item.view.*
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import android.content.Context
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast

//Suuuper nervige Sache
// Falls etwas unklar ist, bitte RecyclerView-Tutorial mal anschauen
class MyAdapter(private val context: Context, private val robots: java.util.ArrayList<Robot>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {


    // ***** Variablen *****
    private lateinit var mListener: OnItemClickListener


    // Hier wird der gerade initialisierte ViewHolder kreiert
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.roboter_item, parent, false)
        return MyViewHolder(itemView, mListener)
    }


    // Hier wird der ViewHolder gebindet, ist son Android Ding, ist dafür da, damit es angezeit werden kann
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = robots[position]
        holder.ivRoboter.setImageResource(currentItem.ivRoboter)
        holder.tvRoboter.text = currentItem.robotName
        holder.tvPfeil.text = currentItem.tvPfeil

        holder.itemView.setOnClickListener {
            currentItem.followme = !currentItem.followme // toggle followme property

            //Ändere die Farbe
            if (currentItem.followme) {
                holder.ivRoboter.setImageResource(R.drawable.b)
            } else {
                holder.ivRoboter.setImageResource(R.drawable.a)
            }

            Toast.makeText(context, "Item $position clicked - Follow me: ${currentItem.followme}", Toast.LENGTH_SHORT).show()
        }

    }

    //Basically wird hier angegben welche Items, der RecyclerView halten soll
    class MyViewHolder(itemView: View, var mListener:OnItemClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        // Das ist, aus was unser Roboter OBjekt besteht
        val ivRoboter : ShapeableImageView = itemView.findViewById(R.id.ivRoboter)
        val tvRoboter : TextView = itemView.findViewById(R.id.tvRoboter)
        val tvPfeil : TextView = itemView.findViewById(R.id.tvPfeil)


        //OnClick Listener
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            if(mListener != null) {
                mListener.setOnClickListener(absoluteAdapterPosition)
            }
        }
    }


    // ***** Funktionen *****
    override fun getItemCount(): Int {
        return robots.size
    }

    fun addRobot(robot: Robot) {
        robots.add(robot)
        notifyItemInserted(robots.size - 1)
    }


    fun deleteRobot() {
        // robots.removeAll { robot -> robots.  }
        // notifyDatSetChanged()
    }

    //Click Listener
    interface OnItemClickListener {
        fun setOnClickListener(pos:Int)
    }

    fun setOnItemClickListener(mListener: OnItemClickListener) {
        this.mListener = mListener
    }


}