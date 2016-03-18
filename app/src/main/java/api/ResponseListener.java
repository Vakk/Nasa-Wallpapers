package api;

/**
 * Created by vakk on 3/16/16.
 */
public interface ResponseListener {
    void done(Object obj);
    void fail(Object obj);
}