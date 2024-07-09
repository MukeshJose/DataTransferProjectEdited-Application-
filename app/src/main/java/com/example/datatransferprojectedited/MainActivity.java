package com.example.datatransferprojectedited;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import com.example.datatransferprojectedited.model.Datum;
import com.example.datatransferprojectedited.model.Root;
import com.example.datatransferprojectedited.retrofit.APIClient;
import com.example.datatransferprojectedited.retrofit.APIInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView rvUserList;
    Button editButton, nextButton, saveButton;
    private ArrayList<Datum> userList = new ArrayList<>();
    private UserAdapter userAdapter;
    private List<Datum> originalUserList = new ArrayList<>();


    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvUserList = findViewById(R.id.rv_employees_list);
        editButton = findViewById(R.id.bt_update_button);
        nextButton = findViewById(R.id.bt_next_button);
        saveButton = findViewById(R.id.bt_save_button);
        searchView = findViewById(R.id.sv_searchBar);

        saveButton.setVisibility(View.GONE);

        fetchDataFromAPI();
//        List<Datum> userList = loadData();
//
//        if (userList.isEmpty()) {
//            userList.clear();
//            fetchDataFromAPI();
//        }

        rvUserList.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(getApplicationContext(), userList);
        rvUserList.setAdapter(userAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterData(query);
//                userAdapter.filter(query);
                saveButton.setVisibility(View.GONE);
                editButton.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterData(newText);
//                userAdapter.filter(newText);
                editButton.setVisibility(View.GONE);
                if (newText.isEmpty()) {
                    editButton.setVisibility(View.VISIBLE);
                    saveButton.setVisibility(View.GONE);
                }
                return false;

            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
//                saveFilteredResultsToJsonFile();
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Datum> users = userAdapter.getUserList();
                boolean isValid = true;

                for (Datum user : users) {
                    // Validate Salary
                    if (!user.getSalary().isEmpty()) {
                        try {
                            Integer.parseInt(user.getSalary());
                        } catch (NumberFormatException e) {
                            isValid = false;
                            break;
                        }
                    }

                    // Validate Age
                    if (!user.getAge().isEmpty()) {
                        try {
                            Integer.parseInt(user.getAge());
                        } catch (NumberFormatException e) {
                            isValid = false;
                            break;
                        }
                    }

                    // Additional validations can be added here
                }

                if (isValid) {
                    saveData(users);
                } else {
                    Toast.makeText(MainActivity.this, "Please correct the errors", Toast.LENGTH_SHORT).show();
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayJsonString();
            }
        });
    }

    private void filterData(String query) {
        userList.clear();
        if (query.isEmpty()) {
            userList.addAll(originalUserList); // Restore original list when query is empty
        } else {
            query = query.toLowerCase(Locale.getDefault());
            for (Datum user : originalUserList) {
                if (user.getUsername().toLowerCase(Locale.getDefault()).contains(query)) {
                    userList.add(user);
                }
            }
        }
        userAdapter.notifyDataSetChanged();

    }


    private void saveData(List<Datum> users) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(users);

        try {
            FileOutputStream fos = openFileOutput("users.json", MODE_PRIVATE);
            fos.write(jsonString.getBytes());
            fos.close();
            Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save data", Toast.LENGTH_SHORT).show();
        }
    }

    private List<Datum> loadData() {
        List<Datum> users = new ArrayList<>();

        try {
            FileInputStream fis = openFileInput("users.json");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            String jsonString = sb.toString();

            Gson gson = new Gson();
            Type listType = new TypeToken<List<Datum>>() {
            }.getType();
            users = gson.fromJson(jsonString, listType);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return users;
    }

    private void fetchDataFromAPI() {

        APIInterface APIInterface = APIClient.getClient().create(APIInterface.class);
        APIInterface.getAllUsers().enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root root = response.body();
                    String rawJson = new Gson().toJson(response.body());
                    Log.d(TAG, "Raw API Response: " + rawJson); // Log the raw response

                    if (root != null) {
                        originalUserList.clear();
                        originalUserList.addAll(root.getData());

                        userList.clear();
                        userList.addAll(root.getData());
                        userAdapter.notifyDataSetChanged();
//                        ArrayList<Datum> users = root.getData();
//                        saveDataToJsonFile(users);
//                        displayJsonString();

                    } else {
                        Log.e(TAG, "Root is null");
                    }
                } else {
                    Toast.makeText(MainActivity.this, "API FAILED", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Log.e(TAG, "API Call failed:/t" + t.getMessage(), t);
            }
        });
    }

    //FOR CONVERTING THE JSON TO A STRING AND DISPLAYING ON SCREEN
    private void saveDataToJsonFile() {
        Toast.makeText(this, "Reached saveDataToJsonFile", Toast.LENGTH_SHORT).show();
        // Save the data to a JSON file
        JSONHelper.saveJSON(this, userList, "users.json");
    }

    private void displayJsonString() {
        String jsonString = JSONHelper.loadJSONString(this, "users.json");
        Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
        intent.putExtra("data", jsonString);
        startActivity(intent);
//        jsonTextView.setText(jsonString);
    }

    private void saveFilteredResultsToJsonFile() {
        List<Datum> filteredUserList = userAdapter.getFilteredUserList();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonArray jsonArray = gson.toJsonTree(filteredUserList).getAsJsonArray();
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("users", jsonArray);

        String jsonString = gson.toJson(jsonObject);

        File file = new File(getExternalFilesDir(null), "filtered_users.json");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(jsonString);
            Toast.makeText(this, "Filtered results saved to JSON file", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save JSON file", Toast.LENGTH_SHORT).show();
        }
    }
}
