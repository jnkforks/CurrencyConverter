# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).


### Development
- Improvement: ViewModel, Repository Cleaning Part 2: Remove Currency Streaming Logic from Repository.
- Improvement: ViewModel, Repository Cleaning Part 2: Remove Change Base Currency logic from Repository.
- Improvement: ViewModel, Repository Cleaning Part 1: Remove Change Calculation Value logic from Repository.
- Improvement: Dagger Singleton Provider modules changed to object.
- Improvement: Retry interval removed from ViewModel
- Fix: CurrencyValueCalculator implemented.
- Fix: In the landscape mode user can't use fractional digits
- Fix: Selection doesn't apply

### Ararat[1.0.0] - 2020-06-12
- App Name and Title change.
- Text overlapping issue fixed.
- Implement Calculation Value Case.
- Implement Base Value change in Base Currency Change Case.
- Activity Espresso Automation Test implemented.
- DaggerMock Integration
- Missing Currency Selection Fragment Test implemented.
- Increase code coverage of CurrencyCalculatorRepositoryConfigurationImpl
- Remove mapping responsibility to Repository
- Ktlint integrated
- Proguard activated
- Implement Base Item Change Util
- Show Currency Calculator in MainActivity
- Implement The Design of Currency Calculator
- Implement Country Code Mapper
- Refactoring in Package and Namings
- Implement Currency Rate List Fragment
- Implement Currency Rate Repository
- Implement Service Layer with Retrofit