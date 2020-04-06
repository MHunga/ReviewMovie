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
  ```
  ## *Đăng nhập
  ![Image description](https://i.imgur.com/SDWJefb.png?1)
  * Có thể đăng nhập bằng Facebook, Google, Phone
 ```java
 private void logInWithEmail(String email, String password) {
        Utils.SetProgressDialogIndeterminate(this, "Đăng nhập");
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            if (firebaseUser.isEmailVerified()) {
                                Utils.UnSetProgressDialogIndeterminate();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                txtFailLogin.setText("Tài khoản của bạn chưa được xác minh");

                                Utils.UnSetProgressDialogIndeterminate();
                                txtResendVerify.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
    }
   ```
   ## * Home
   ![Image description](https://i.imgur.com/bVGVUq2.png?1)
   * Hiển thị các mục ***Trending, Phim đang chiếu, Phim sắp công chiếu***
   * Sử dụng **Retrofit2** đọc dữ liệu Api
   * [Nguồn api](https://www.themoviedb.org/)
   ## * Thông tin chi tiết
   ![Image description](https://i.imgur.com/8uMNuE0.png?1)      ![Image description](https://i.imgur.com/03J0aA6.png?1)
   * Hiển thị thông tin chi tiết bộ phim và đánh giá của người dùng
   ## * Tìm kiếm
   ![Image description](https://i.imgur.com/YQugUgA.png?1)
   * tìm kiếm theo danh mục hoặc tìm kiếm theo từ khóa
   * ![Image description](https://i.imgur.com/PISd5eG.png?1)
   ## * Thông báo
   ![Image description](https://i.imgur.com/JzEQOpu.png?1)
   * Hiển thị thông báo qua cơ chế **Firebase Cloud Messaging**
   ## * Profile
   ![Image description](https://i.imgur.com/ZYMWEs7.png?1)
   ![Image description](https://i.imgur.com/VCJJ0bK.png?1)
   
    
