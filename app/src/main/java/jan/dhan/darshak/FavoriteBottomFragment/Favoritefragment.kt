package jan.dhan.darshak.FavoriteBottomFragment

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentContainer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import jan.dhan.darshak.adapter.FavoriteAdapter
import jan.dhan.darshak.database.Favoriteentity
import jan.dhan.darshak.database.favoriteplacesapp
import jan.dhan.darshak.databinding.FavoriteBottomFragmentBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class Favoritefragment(private val mContext:Context):BottomSheetDialogFragment() {
    private var _binding: FavoriteBottomFragmentBinding? = null
    private val binding get() = _binding!!

    private var list = arrayListOf<Favoriteentity>()

    private val dao=(mContext.applicationContext as favoriteplacesapp).db.favoritedao()
    private var favoriteplaceAdapter: FavoriteAdapter?= FavoriteAdapter(list, context)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FavoriteBottomFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
        dao.fetchAllfavorites().collect { data ->
            list = data as ArrayList<Favoriteentity>
            if(list.isNotEmpty())  {
                binding.rvFavoriteplaces.layoutManager=LinearLayoutManager(view.context)
                favoriteplaceAdapter = FavoriteAdapter(list, context)
                binding.rvFavoriteplaces.adapter = favoriteplaceAdapter
            }
        }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}