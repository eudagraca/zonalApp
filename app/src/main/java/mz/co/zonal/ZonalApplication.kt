package mz.co.zonal

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import mz.co.zonal.service.db.ZonalDatabase
import mz.co.zonal.service.db.preferences.PreferencesProvider
import mz.co.zonal.service.factory.*
import mz.co.zonal.service.model.Session
import mz.co.zonal.service.network.NetworkControl
import mz.co.zonal.service.network.ZonalFirebase
import mz.co.zonal.service.network.ZonalRestAPI
import mz.co.zonal.service.repository.*
import mz.co.zonal.utils.CategoryViewModelFactory
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
        bind() from singleton { PreferencesProvider(instance()) }
        bind() from singleton { UserRepository(instance(), instance()) }
        bind() from singleton { Session(instance()) }
        bind() from singleton { UserViewModelFactory(instance(), instance()) }
        bind() from singleton { CategoryViewModelFactory(instance()) }
        bind() from singleton { ProductRepository(instance(), instance(), instance()) }
        bind() from singleton { CategoryRepository(instance(), instance(), instance()) }
        bind() from singleton { ProductViewModelFactory(instance(), instance()) }
        bind() from singleton { TypeViewModelFactory(instance(), instance()) }
        bind() from singleton { TypeRepository(instance()) }
        bind() from singleton { BrandRepository(instance()) }
        bind() from singleton { BrandViewModelFactory(instance()) }
        bind() from singleton { CurrencyViewModelFactory(instance()) }
        bind() from singleton { CurrencyRepository(instance()) }
        bind() from singleton { MessageViewModelFactory(instance()) }
        bind() from singleton { MessageRepository(instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}