package com.gzq.lib_core.http.exception;


import com.google.gson.reflect.TypeToken;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.model.BaseModel;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;

/**
 * 加入了对错误处理，已经比较完整了
 */
public class ErrorTransformer<T> implements ObservableTransformer<BaseModel<T>, T> {

    public static <T> ErrorTransformer<T> create() {
        return new ErrorTransformer<>();
    }

    private static ErrorTransformer instance = null;

    private ErrorTransformer() {
    }

    /**
     * 双重校验锁单例(线程安全)
     */
    public static <T> ErrorTransformer<T> getInstance() {
        if (instance == null) {
            synchronized (ErrorTransformer.class) {
                if (instance == null) {
                    instance = new ErrorTransformer<>();
                }
            }
        }
        return instance;
    }

    @Override
    public ObservableSource<T> apply(Observable<BaseModel<T>> responseObservable) {
        return responseObservable.flatMap(new Function<BaseModel<T>, Observable<T>>() {
            @Override
            public Observable<T> apply(BaseModel<T> httpResult) throws Exception {
                if (httpResult.getCode() != ErrorType.SUCCESS) {
                    return Observable.error(new ServerException(httpResult.getMessage(), httpResult.getCode()));
                }
                T data = httpResult.getData();
                if (data == null) {
                    Throwable error = new RuntimeException("data == null");
                    try {
                        try {
                            data = (T) new ArrayList<>();
//                            Type type = new TypeToken<T>() {}.getType();
//                            data = Box.getGson().fromJson("[]", type);
                        } catch (Throwable e1) {
                            e1.printStackTrace();
                            data = null;
                        }
                        if (data == null) {
                            Type type = new TypeToken<T>() {}.getType();
                            data = Box.getGson().fromJson("{}", type);
                        }

                        if (data != null) {
                            return Observable.just(data);
                        }
                    } catch (Throwable e) {
                        error = e;
                    }
                    return Observable.error(new RuntimeException("未知异常", error));
                }
                return Observable.just(data);
            }
        }).onErrorResumeNext(new Function<Throwable, ObservableSource<? extends T>>() {
            @Override
            public ObservableSource<? extends T> apply(Throwable throwable) throws Exception {
                //ExceptionEngine为处理异常的驱动器
                throwable.printStackTrace();
                return Observable.error(ExceptionEngine.handleException(throwable));
            }
        });
    }


    static Class<?> getRawType(Type type) {
//        checkNotNull(type, "type == null");

        if (type instanceof Class<?>) {
            // Type is a normal class.
            return (Class<?>) type;
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;

            // I'm not exactly sure why getRawType() returns Type instead of Class. Neal isn't either but
            // suspects some pathological case related to nested classes exists.
            Type rawType = parameterizedType.getRawType();
            if (!(rawType instanceof Class)) throw new IllegalArgumentException();
            return (Class<?>) rawType;
        }
        if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            return Array.newInstance(getRawType(componentType), 0).getClass();
        }
        if (type instanceof TypeVariable) {
            // We could use the variable's bounds, but that won't work if there are multiple. Having a raw
            // type that's more general than necessary is okay.
            return Object.class;
        }
        if (type instanceof WildcardType) {
            return getRawType(((WildcardType) type).getUpperBounds()[0]);
        }

        throw new IllegalArgumentException("Expected a Class, ParameterizedType, or "
                + "GenericArrayType, but <" + type + "> is of type " + type.getClass().getName());
    }
}

