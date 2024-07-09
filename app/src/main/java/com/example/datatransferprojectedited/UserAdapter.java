package com.example.datatransferprojectedited;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Filterable;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datatransferprojectedited.model.Datum;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> implements Filterable {
    private List<Datum> userList;
    private List<Datum> userListFiltered;
    Context context;

    public UserAdapter(Context context, List<Datum> userList) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_custom_layout, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Datum user = userList.get(position);
        holder.bind(user);
//        holder.userName.setText(user.getUsername());
//        holder.userSalary.setText(String.valueOf(user.getSalary()));
//        holder.userAge.setText(String.valueOf(user.getAge()));
//        holder.userPhoneNumber.setText(user.getPhoneNumber());

        //VALIDATION FOR USERNAME

        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isLetter(source.charAt(i)) && !Character.isSpaceChar((source.charAt(i)))) {
                        return "";
                    }
                }
                return null;
            }
        };

// Apply the filter to the EditText
        holder.userName.setFilters(new InputFilter[]{filter});
        if (user.getDob() != null && !user.getDob().isEmpty()) {
            holder.userDOB.setText(user.getDob());
        }

        holder.userDOB.setFocusable(false);
        holder.userDOB.setClickable(true);


        holder.userDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(holder.userDOB, user);
            }
        });

//        holder.userSalary.addTextChangedListener(new SimpleTextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                user.setSalary(s.toString());
//            }
//        });
//
//        holder.userAge.addTextChangedListener(new SimpleTextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                user.setAge(s.toString());
//            }
//        });
//
//        holder.userPhoneNumber.addTextChangedListener(new SimpleTextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                user.setPhoneNumber(s.toString());
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    public List<Datum> getUserList() {
        return userList;
    }

    //    public void filter(String query) {
//        if (query == null || query.isEmpty()) {
//            userListFiltered = new ArrayList<>(userList);
//        } else {
//            List<Datum> filteredList = new ArrayList<>();
//            for (Datum user : userList) {
//                if (user.getUsername().toLowerCase().contains(query.toLowerCase())) {
//                    filteredList.add(user);
//                }
//            }
//            userListFiltered = filteredList;
//        }
//        notifyDataSetChanged();
//    }
    public void filter(String query) {
        query = query.toLowerCase(Locale.getDefault());
        List<Datum> filteredList = new ArrayList<>();
        if (query.isEmpty()) {
            filteredList.addAll(userList);
        } else {
            for (Datum user : userList) {
                if (user.getUsername().toLowerCase(Locale.getDefault()).contains(query)) {
                    filteredList.add(user);
                }
            }
        }
        userList.clear();
        userList.addAll(filteredList);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    userListFiltered = userList;
                } else {
                    List<Datum> filteredList = new ArrayList<>();
                    for (Datum datum : userList) {
                        if (datum.getUsername().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(datum);
                        }
                    }
                    userListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = userListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                userListFiltered = (ArrayList<Datum>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public List<Datum> getFilteredUserList() {
        return userListFiltered;
    }


    private void showDatePickerDialog(EditText userDOB, Datum user) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(userDOB.getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = year + "-" + (month + 1) + "-" + dayOfMonth;
                        userDOB.setText(date);
                        user.setDob(date);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        EditText userName, userSalary, userAge, userPhoneNumber, userDOB;
        private TextWatcher salaryTextWatcher;
        private TextWatcher ageTextWatcher;
        private TextWatcher phoneNumberTextWatcher;
        private TextWatcher dobTextWatcher;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.et_user_name);
            userSalary = itemView.findViewById(R.id.et_salary);
            userAge = itemView.findViewById(R.id.et_age);
            userPhoneNumber = itemView.findViewById(R.id.et_phone_number);
            userDOB = itemView.findViewById(R.id.et_dob);
        }

        public void bind(Datum user) {

            // Remove existing text watchers
            if (salaryTextWatcher != null) {
                userSalary.removeTextChangedListener(salaryTextWatcher);
            }
            if (ageTextWatcher != null) {
                userAge.removeTextChangedListener(ageTextWatcher);
            }
            if (phoneNumberTextWatcher != null) {
                userPhoneNumber.removeTextChangedListener(phoneNumberTextWatcher);
            }
            if (dobTextWatcher != null) {
                userDOB.removeTextChangedListener(dobTextWatcher);
            }

            userName.setText(user.getUsername());
            userSalary.setText(String.valueOf(user.getSalary()));
            userAge.setText(String.valueOf(user.getAge()));
            userPhoneNumber.setText(user.getPhoneNumber());
            userDOB.setText(user.getDob());
            userName.addTextChangedListener(new SimpleTextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    userList.get(getAdapterPosition()).setUsername(s.toString());
                }
            });

            userSalary.addTextChangedListener(new SimpleTextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    userList.get(getAdapterPosition()).setSalary(s.toString());
                }
            });

            userAge.addTextChangedListener(new SimpleTextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    userList.get(getAdapterPosition()).setAge(s.toString());
                }
            });
            userPhoneNumber.addTextChangedListener(new SimpleTextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    userList.get(getAdapterPosition()).setPhoneNumber(s.toString());
                }
            });

            userDOB.addTextChangedListener(new SimpleTextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    userList.get(getAdapterPosition()).setDob(s.toString());
                }
            });
        }
    }
}
//            userSalary.addTextChangedListener(salaryTextWatcher);
//
//            ageTextWatcher = new TextWatcher() {
//                @Override
//                public void afterTextChanged(Editable s) {
//                    user.setAge(s.toString());
//                }
//
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                }
//            };
//            userAge.addTextChangedListener(ageTextWatcher);
//
//            phoneNumberTextWatcher = new TextWatcher() {
//                @Override
//                public void afterTextChanged(Editable s) {
//                    user.setPhoneNumber(s.toString());
//                }
//
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                }
//            };
//            userPhoneNumber.addTextChangedListener(phoneNumberTextWatcher);
//
//            dobTextWatcher = new TextWatcher() {
//                @Override
//                public void afterTextChanged(Editable s) {
//                    user.setDob(s.toString());
//                }
//
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                }
//            };
//            userDOB.addTextChangedListener(dobTextWatcher);
//        }
//    }
//}



