# CurrencyConverter
CurrencyConverter is yet another sample Android Project which utilize recent Android Development Tech Stack from my developer point of view.

The goal of the project is to practise [Kotlin Flow](https://kotlinlang.org/docs/reference/coroutines/flow.html) and show the 1 second interval updated currency list in [RecyclerView](https://developer.android.com/guide/topics/ui/layout/recyclerview).
The software components in the project are testable and developed by using SOLID principles.


<a href="https://www.youtube.com/watch?v=XUpJKXovy9w">
<img src="https://github.com/ibrahimyilmaz/CurrencyConverter/blob/develop/art/simple_screenshot.png" width="250">
</a>


# Tech Stack
-  [Kotlin (100%)](https://kotlinlang.org/docs/reference/)
-  [Kotlin Flow](https://kotlinlang.org/docs/reference/coroutines/flow.html)
-  [Android Architecture Components](https://developer.android.com/topic/libraries/architecture)
   - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
   - [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
   - [Navigation Component](https://developer.android.com/guide/navigation/navigation-getting-started)
   - [Navigation UI](https://developer.android.com/guide/navigation/navigation-ui)
   - [ViewBinding](https://developer.android.com/topic/libraries/view-binding)
-  Dependency Injection
   - [Dagger2](https://dagger.dev)
-  Testing
   - [Espresso](https://developer.android.com/training/testing/espresso)
   - [FragmentScenario](https://developer.android.com/training/basics/fragments/testing)
   - [Mockito](https://site.mockito.org)
   - [Mockito-Kotlin](https://github.com/nhaarman/mockito-kotlin)
   - [DaggerMock](https://github.com/fabioCollini/DaggerMock)
-  Http
   - [Retrofit](https://github.com/square/retrofit)
   - [Moshi](https://github.com/square/retrofit)
-  Memory Analyzer
   - [LeakCanary](https://github.com/square/leakcanary)
   
# TODO
- [ ] Explain App Architecture
- [ ] Use last received co efficients and use them in Offline Case
