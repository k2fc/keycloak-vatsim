# keycloak-vatsim

Keycloak Social Login extension for Vatsim.


## Install

Download `keycloak-vatsim-<version>.jar` from [Releases page](https://github.com/k2fc/keycloak-vatsim/releases).
Then deploy it into `$KEYCLOAK_HOME/providers` directory.

## Setup

### VATSIM

Access to an OAuth Organization on VATSIM [VATSIM Connect](https://auth.vatsim.net/organization/requirements) 
to create your application.

You can get Client ID and Client Secret from the created application.

### Keycloak

Note: You don't need to setup the theme in `master` realm from v0.3.0.

1. Add `vatsim` Identity Provider in the realm which you want to configure.
2. In the `vatsim` identity provider page, set `Client Id` and `Client Secret`.

## Source Build

Clone this repository and run `mvn package`.
You can see `keycloak-vatsim-<version>.jar` under `target` directory.


## Licence

[Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0)


## Author

- [Dennis Graiani](https://github.com/k2fc)

Based on vatsim-discord by [Hiroyuki Wada](https://github.com/wadahiro)

