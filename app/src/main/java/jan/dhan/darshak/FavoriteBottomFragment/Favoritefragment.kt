package jan.dhan.darshak.FavoriteBottomFragment

import android.app.Application
import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentContainer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import jan.dhan.darshak.R
import jan.dhan.darshak.adapter.FavoriteAdapter
import jan.dhan.darshak.database.Favoriteentity
import jan.dhan.darshak.database.favoriteplacesapp
import jan.dhan.darshak.databinding.FavoriteBottomFragmentBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class Favoritefragment(private val mContext:Context):BottomSheetDialogFragment() {
    private var _binding: FavoriteBottomFragmentBinding? = null
    private val binding get() = _binding!!
    private var list = arrayListOf<Favoriteentity>()
    private val dao=(mContext.applicationContext as favoriteplacesapp).db.favoritedao()
    private var favoriteplaceAdapter: FavoriteAdapter?=FavoriteAdapter(list, context
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FavoriteBottomFragmentBinding.inflate(inflater, container, false)
        binding.ivclose.setOnClickListener{
                this.dismiss()
            Toast.makeText(mContext,"clicked",Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
           val simpleCallback = object: ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT){
               val deleteColor=ContextCompat.getColor(mContext, R.color.marker_color_selected)
               val deleteIcon=R.drawable.ic_baseline_delete_24
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
              return false
            }
               override fun onChildDraw(
                   c: Canvas,
                   recyclerView: RecyclerView,
                   viewHolder: RecyclerView.ViewHolder,
                   dX: Float,
                   dY: Float,
                   actionState: Int,
                   isCurrentlyActive: Boolean
               )
               {
                RecyclerViewSwipeDecorator.Builder(c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(deleteColor)
                    .addSwipeLeftActionIcon(deleteIcon)
                    .create()
                    .decorate()

                   super.onChildDraw(
                       c,
                       recyclerView,
                       viewHolder,
                       dX,
                       dY,
                       actionState,
                       isCurrentlyActive
                   )

               }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
               val position=viewHolder.absoluteAdapterPosition
                var flag=true
                when(direction){
                    ItemTouchHelper.LEFT->{
                        val removed=list.get(position)
                        val name=removed.name
                        val id=removed.id
                        list.removeAt(position)
                       //deleterecord(id)
                        binding.rvFavoriteplaces.adapter?.notifyItemRemoved(position)
                      Snackbar.make(binding.rvFavoriteplaces.rootView,name+" is Deleted",Snackbar.LENGTH_LONG).setAction("Undo"
                              ,View.OnClickListener{
                                list.add(position,removed)
                              binding.rvFavoriteplaces.adapter?.notifyItemInserted(position)
                                flag=false
                          }).show()
                        lifecycleScope.launch {
                            delay(5000)
                            if (flag) {
                                Log.d("asd", "yoooo")
                                deleterecord(id)
                            }
                        }
                    }
                }

            }

           }
        lifecycleScope.launch {
        dao.fetchAllfavorites().collect { data ->
            list = data as ArrayList<Favoriteentity>
            if (list.isNotEmpty()) {
                binding.ivNoDataIcon.visibility=View.GONE
                binding.rvFavoriteplaces.visibility=View.VISIBLE
                binding.tvEmptylist.visibility=View.GONE
                if (list.isNotEmpty()) {
                    binding.rvFavoriteplaces.layoutManager = LinearLayoutManager(view.context)
                    favoriteplaceAdapter = FavoriteAdapter(
                        list, context
                    )
                    binding.rvFavoriteplaces.adapter = favoriteplaceAdapter
                    val itemTouchHelper=ItemTouchHelper(simpleCallback)
                    itemTouchHelper.attachToRecyclerView(binding.rvFavoriteplaces)
                }
            }
            else
            {
                binding.ivNoDataIcon.visibility=View.VISIBLE
                binding.rvFavoriteplaces.visibility=View.GONE
                binding.tvEmptylist.visibility=View.VISIBLE
            }
        }

        }
    }
    fun deleterecord(id: String)
    {
        lifecycleScope.launch{
            dao.Delete(id)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}