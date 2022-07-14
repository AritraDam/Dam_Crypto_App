package com.example.cryptoapp.fragment

 import android.opengl.Visibility
 import android.os.Bundle
 import android.util.Log
 import android.view.LayoutInflater
import android.view.View
 import android.view.View.GONE
 import android.view.View.VISIBLE
 import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
 import androidx.viewpager2.widget.ViewPager2
 import com.example.cryptoapp.adapter.TopLossGainPagerAdapter
 import com.example.cryptoapp.adapter.TopMarketAdapter
 import com.example.cryptoapp.api.ApiInterface
 import com.example.cryptoapp.api.ApiUtilities
 import com.example.cryptoapp.databinding.FragmentHomeBinding
 import com.google.android.material.tabs.TabLayoutMediator
 import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
 import kotlinx.coroutines.withContext
 import retrofit2.create

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)


        getCryptoCurrencyList()
        setTabLayout()

        return binding.root


    }

    private fun setTabLayout() {
        val adapter = TopLossGainPagerAdapter(this)
        binding.contentViewPager.adapter = adapter
        binding.contentViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if(position == 0){
                    binding.topGainIndicator.visibility = VISIBLE
                    binding.topLoseIndicator.visibility = GONE
                }
                else{
                    binding.topGainIndicator.visibility = GONE
                    binding.topLoseIndicator.visibility = VISIBLE
                }
            }
        })

        TabLayoutMediator(binding.tabLayout,binding.contentViewPager){
            tab,position ->
            var title = if (position == 0){
                "Top Gainers"

            }
            else{
                "Top Losers"
            }
            tab.text = title
        }.attach()

    }

    private fun getCryptoCurrencyList() {
        lifecycleScope.launch(Dispatchers.IO){
            val res = ApiUtilities.getInstance().create(ApiInterface::class.java).getMarketData()

            //binding list with adapter

            withContext(Dispatchers.Main){
                binding.topCurrencyRecyclerView.adapter = TopMarketAdapter(requireContext(),res.body()!!.data
                    .cryptoCurrencyList)
            }
//            Log.d("Aritra", "getCryptoCurrencyList:${res.body()!!.data.cryptoCurrencyList} ")
        }
    }

}