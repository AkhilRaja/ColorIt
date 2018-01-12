package arkratos.gamedev.com.colirfy;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class Store extends AppCompatActivity implements MyDialog.NoticeDialogListener {

    private StorageReference mStorageRef;
    private DatabaseReference mRef;

    FirebaseAuth mAuth;
    RecyclerView mrecyclerView;
    FirebaseRecyclerAdapter adapter;
    /*in App */
    BillingManager billingManager;
    int a;
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onDialogPositiveClick(MyDialog dialog) {

    }

    public class MyBillingUpdateListener implements BillingManager.BillingUpdatesListener {
        @Override
        public void onBillingClientSetupFinished() {
                billingManager.queryPurchases();
        }

        @Override
        public void onConsumeFinished(String token, int result) {

            if (result == BillingClient.BillingResponse.OK) {

            }
        }

        @Override
        public void onPurchasesUpdated(List<Purchase> purchases) {

            for (Purchase p : purchases) {
                //update ui
                Log.d("Purchased ",p.getOrderId());
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);



        /*IN aAPP */
//        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhC5R3z+dU29H2ozwjdupEFjxByVNhjYXucEgnIoVP9wv3SWZ2S7hGM1NdijqzxbWijQPMcaVFjykkitCIJlibeVRGZ6iSRtHT2zE0Wl73jOaFMlebShbAczfj19A4tbmQ22hyxmth1pI/Yf59SAztFp12lBVVxC2zEmA3Wr+Cphx28E/MvvKX19hX20AuPMIQJ+IHNIHsPgwk/BslbmjhxeVB/SiPKQAN28TMeupYymi0kAYq6KDRm40LLMiBhm4tiUNqfgPl/stnzeEbOfE88SfMAid93Nia8K9U7mCJDRxSlJF5X+mXy9s8PirFHLqbFgGyiebE2yqbkEMLGBWvwIDAQAB";


        billingManager = new BillingManager(Store.this, new MyBillingUpdateListener());


        /*
        Fire base Authentication
        */

        mAuth = FirebaseAuth.getInstance();
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Firebase", "signInAnonymously:success");



                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Firebase", "signInAnonymously:failure", task.getException());
                            Toast.makeText(Store.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        /*
        Fire base Database
        */

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference();
        mRef.keepSynced(true);

        /*
        Creating the Recycler Views
        */


        mrecyclerView = (RecyclerView) findViewById(R.id.recyclerview_store);
        mrecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext()
                ,1,GridLayoutManager.HORIZONTAL,false));

        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(mrecyclerView);
        /*
        Firebase Adapters
        */
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Categories")
                .limitToLast(5);


        FirebaseRecyclerOptions<Categories> options =
                new FirebaseRecyclerOptions.Builder<Categories>()
                        .setQuery(query, Categories.class)
                        .build();

         adapter = new FirebaseRecyclerAdapter<Categories, MyViewHolder>(options) {



             @Override
            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.store_categorylist, parent, false);
                Log.d("Inside Create","Success");
                return new MyViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(MyViewHolder holder, int position, Categories model) {
                // Bind the Chat object to the ChatHolder
                // ...
                 //Log.d("Inside OnBindView",model.getCategoryname());
                 holder.textView.setText(model.getCategoryname());
                 GlideApp.with(getApplicationContext())
                        .load(model.getImage())
                        .into(holder.imageView);
                Log.d("Inside Bind","success");
            }
        };

        mrecyclerView.setAdapter(adapter);

        /*

        Fire Base Cloud Storage

        */

        FirebaseStorage storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getReference();

        mStorageRef.child("Placid/003.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("URL", "MyDownloadLink:  " + uri);
            }
        });

    }
    /*

    View holder Class for Recycler View

    */
    public class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        private TextView textView;
        private ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView  = (TextView) itemView.findViewById(R.id.storeList_text);
            imageView = (ImageView) itemView.findViewById(R.id.storeList_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //This line brings up the paid app purchase Flow
           // billingManager.initiatePurchaseFlow("art", null, BillingClient.SkuType.INAPP);

            /* if Successfully bought then you should download these image packs */

            final List<String> skuList = new ArrayList<>();
            skuList.add("art");
            skuList.add("dreams");

            SkuDetailsResponseListener skuDetailsResponseListener = new SkuDetailsResponseListener() {
                @Override
                public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
                    //Get to know all the products this guy bought. So its always better to download things from here.
                    //1.Always need to check if this guy has a copy of all things here or not
                    //2.Need to check if this callback is called after the purchase is done also or not

                    Log.d("Products",skuDetailsList.get(0).getSku());
                }
            };
            MyDialog dialog = new MyDialog();
            dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
          //  billingManager.querySkuDetailsAsync(skuList,skuDetailsResponseListener);
        //    billingManager.consumeAsync("obcaeiinplckfnmfjhfidfhd.AO-J1OzmEtuNCLEPZ4NP-HNM8bdwej3WYPrVe33gYhapWPrCsghNKol3AZjzCXChZiqqqmie8tBnh1o9EGaPh5WFIyLt01eT4o0iUNeWM2lZl-amHw3bzLI");
        }
    }


}


