package com.shottracker.utils

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

/**
 * LiveData that emits a value only once per a single observer.
 * E.g. when you re-subscribe to this LiveEvent on phone rotation
 * it wouldn't re-emit a value that it'd previously emitted.
 * {@see https://proandroiddev.com/livedata-with-single-events-2395dea972a8}
 */
class LiveEvent<T> : MediatorLiveData<T>() {

    private val observers = HashSet<ObserverWrapper<in T>>()

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        val wrapper = ObserverWrapper(observer)
        observers.add(wrapper)
        super.observe(owner, wrapper)
    }

    @MainThread
    override fun observeForever(observer: Observer<in T>) {
        val wrapper = ObserverWrapper(observer)
        observers.add(wrapper)
        super.observeForever(wrapper)
    }

    @MainThread
    override fun removeObserver(observer: Observer<in T>) {
        val wrapper = ObserverWrapper(observer)
        if (observers.remove(wrapper)) {
            super.removeObserver(observer)
            return
        }
        val iterator = observers.iterator()
        while (iterator.hasNext()) {
            val wrapper2 = iterator.next()
            if (wrapper2.observer == observer) {
                iterator.remove()
                super.removeObserver(wrapper2)
                break
            }
        }
    }

    @MainThread
    override fun setValue(t: T?) {
        observers.forEach { it.newValue() }
        super.setValue(t)
    }

    private class ObserverWrapper<T>(val observer: Observer<T>) : Observer<T> {

        private var pending = false

        override fun onChanged(t: T) {
            if (pending) {
                pending = false
                observer.onChanged(t)
            }
        }

        fun newValue() {
            pending = true
        }
    }

}