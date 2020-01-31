package mz.co.zonal

import android.app.Application
import mz.co.zonal.service.db.ZonalDatabase
import mz.co.zonal.service.network.NetworkControl
import mz.co.zonal.service.network.ZonalRestAPI
import mz.co.zonal.service.repository.UserRepository
import mz.co.zonal.utils.UserViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class ZonalApplication : Application(), KodeinAware{
    override val kodein = Kodein.lazy {
        import(androidXModule(this@ZonalApplication))

        bind() from singleton { NetworkControl(instance()) }
        bind() from singleton { ZonalRestAPI(instance()) }
        bind() from singleton { ZonalDatabase(instance()) }
        bind() from singleton { UserRepository(instance(), instance()) }
        bind() from singleton { UserViewModelFactory(instance()) }
    }

}