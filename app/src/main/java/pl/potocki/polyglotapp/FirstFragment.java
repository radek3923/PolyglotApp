package pl.potocki.polyglotapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import pl.potocki.polyglotapp.databinding.FragmentFirstBinding;
import pl.potocki.polyglotapp.randomWord.api.RandomWordApi;
import pl.potocki.polyglotapp.randomWord.api.RandomWordApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        System.out.println("test1");

//        DeepLApiService deepLApiService = DeepLApi.getRetrofitInstance().create(DeepLApiService.class);
//        System.out.println("test2");
//        Call<LanguageData> call = deepLApiService.getLanguageData();
//        System.out.println("test3");
//        call.enqueue(new Callback<LanguageData>() {
//            @Override
//            public void onResponse(Call<LanguageData> call, Response<LanguageData> response) {
//                System.out.println(response.body());
//            }
//
//            @Override
//            public void onFailure(Call<LanguageData> call, Throwable t) {
//
//            }
//        });

        RandomWordApiService randomWordApiService = RandomWordApi.getRetrofitInstance().create(RandomWordApiService.class);
        Call<String[]> call = randomWordApiService.getRandomWords();
        call.enqueue(new Callback<String[]>() {
            @Override
            public void onResponse(Call<String[]> call, Response<String[]> response) {
                System.out.println();
                String words[] = response.body();
                for (String word : words){
                    System.out.println(word);
                }
            }

            @Override
            public void onFailure(Call<String[]> call, Throwable t) {
                return;
            }
        });

        String[] languages = {"English", "Polish", "Spanish"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.sourceLangSpinner.setAdapter(adapter);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void sendMessage(View view) {
        System.out.println("Test");
    }

}