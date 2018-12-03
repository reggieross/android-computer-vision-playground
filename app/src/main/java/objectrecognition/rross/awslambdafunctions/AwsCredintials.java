package objectrecognition.rross.awslambdafunctions;

import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;

public class AwsCredintials {

    private String COGNITO_POOL_ID = "us-east-1:4f3d494f-f78f-4165-9e42-1c2b8cec3fd0\n";
    private CognitoCachingCredentialsProvider credentialsProvider;
    private Context context;
    private Regions region;

    public AwsCredintials(
        Context context,
        Regions region
    ) {
        this.context = context;
        this.region = region;
        credentialsProvider = new CognitoCachingCredentialsProvider(
            context,
            COGNITO_POOL_ID,
            region
        );
    }

    public CognitoCachingCredentialsProvider getCredentialsProvider() {
        return credentialsProvider;
    }

    public Context getContext() {
        return context;
    }

    public Regions getRegion() {
        return region;
    }
}
