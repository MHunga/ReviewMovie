# Review Movie
## Review Movie
### Là ứng dụng liệt kê danh sách các phim đang thịnh hành, phim đang chiếu , phim sắp công chiếu, ..vv..  
![Image description](https://i.imgur.com/fH3mAW8.png?2)
## * Thiết lập tài khoản user
![Image description](https://i.imgur.com/Z3OHphg.png?1)
#### Thông tin được đăng ký trên FirebaseAuthentication, và dữ liệu user được lưu tại RealtimeDatabase

```java 
private void register(final String email, final String password, final String name) {
        try {
            showAlertDialog("Đăng ký");
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        firebaseAuth.getCurrentUser().sendEmailVerification()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            unShowAlertDialog();
                                            Intent intent = new Intent(getActivity().getBaseContext(), EmailVerificationActivity.class);
                                            intent.putExtra("email", email);
                                            intent.putExtra("password", password);
                                            intent.putExtra("name", name);
                                            getActivity().startActivity(intent);
                                        } else {
                                            unShowAlertDialog();
                                            Toast.makeText(getActivity().getBaseContext(), "Can not send verify mail", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                    } else {
                        unShowAlertDialog();
                        Toast.makeText(getActivity().getBaseContext(), "Can not create account", Toast.LENGTH_LONG).show();
                    }
                }
            });

        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
    }
   
    
