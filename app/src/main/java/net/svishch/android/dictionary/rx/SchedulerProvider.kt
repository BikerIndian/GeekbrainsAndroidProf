package net.svishch.android.dictionary.rx

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import net.svishch.android.dictionary.rx.ISchedulerProvider

//In the sake of testing
class SchedulerProvider : ISchedulerProvider {

    override fun ui(): Scheduler = AndroidSchedulers.mainThread()

    override fun io(): Scheduler = Schedulers.io()
}
