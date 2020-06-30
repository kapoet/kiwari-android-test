package com.ervin.kiwariandroidtest.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.ervin.kiwariandroidtest.data.remote.ApiResponse;
import com.ervin.kiwariandroidtest.helpers.AppExecutors;
import com.ervin.kiwariandroidtest.valueobject.Resource;


public abstract class NetworkBoundResource<ResultType, RequestType> {
    private MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

    private AppExecutors mExecutors;

    public NetworkBoundResource(AppExecutors appExecutors) {

        this.mExecutors = appExecutors;
        result.setValue(Resource.loading(null));

        LiveData<ResultType> dbSource = loadFromDB();

        result.addSource(dbSource, data -> {
            result.removeSource(dbSource);
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource);
            } else {
                result.addSource(dbSource, newData -> result.setValue(Resource.success(newData)));
            }
        });

    }

    private void fetchFromNetwork(LiveData<ResultType> dbSource) {

        LiveData<ApiResponse<RequestType>> apiResponse = createCall();

        result.addSource(dbSource, newData ->
                result.setValue(Resource.loading(newData))
        );
        result.addSource(apiResponse, response -> {

            result.removeSource(apiResponse);
            result.removeSource(dbSource);

            switch (response.status) {
                case SUCCESS:
                    mExecutors.diskIO().execute(() -> {
//ini dia nyimpan di db, jadi savecallresult mesti diisi.. kalau tidak mau masuk ke db langsung saja akses ke remoteRepository
                        saveCallResult(response.body);

                        mExecutors.mainThread().execute(() ->
                                result.addSource(loadFromDB(),
                                        newData -> result.setValue(Resource.success(newData))));

                    });
                    break;

                case EMPTY:
                    mExecutors.mainThread().execute(() ->
                            result.addSource(loadFromDB(),
                                    newData -> result.setValue(Resource.success(newData))));

                    break;
                case ERROR:
                    onFetchFailed();
                    result.addSource(dbSource, newData ->
                            result.setValue(Resource.error(response.message, newData)));
                    break;
            }
        });
    }

    public LiveData<Resource<ResultType>> asLiveData() {
        return result;
    }

    protected void onFetchFailed() {
    }

    protected abstract LiveData<ResultType> loadFromDB();

    protected abstract Boolean shouldFetch(ResultType data);

    protected abstract LiveData<ApiResponse<RequestType>> createCall();

    protected abstract void saveCallResult(RequestType data);
}
