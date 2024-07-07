package com.example.datatransferprojectedited;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datatransferprojectedited.model.Datum;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<Datum> userList;

    public UserAdapter(List<Datum> userList) {
        this.userList = userList;
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
        holder.userName.setText(user.getUsername());
        holder.userSalary.setText(String.valueOf(user.getSalary()));
        holder.userAge.setText(String.valueOf(user.getAge()));
        holder.userPhoneNumber.setText(user.getPhoneNumber());

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

        holder.userSalary.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                user.setSalary(s.toString());
            }
        });

        holder.userAge.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                user.setAge(s.toString());
            }
        });

        holder.userPhoneNumber.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                user.setPhoneNumber(s.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public List<Datum> getUserList() {
        return userList;
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

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.et_user_name);
            userSalary = itemView.findViewById(R.id.et_salary);
            userAge = itemView.findViewById(R.id.et_age);
            userPhoneNumber = itemView.findViewById(R.id.et_phone_number);
            userDOB = itemView.findViewById(R.id.et_dob);

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
