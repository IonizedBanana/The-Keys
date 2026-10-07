# <img src="documents/icons/life-buoy.svg" width="30" alt=""> The Keys Hurricane Relief App

![Java 11](https://img.shields.io/badge/Java-11-ED8B00?logo=openjdk&logoColor=white)
![JavaFX 17](https://img.shields.io/badge/JavaFX-17-007396)
![Maven](https://img.shields.io/badge/Maven-build-C71A36?logo=apachemaven&logoColor=white)

The Hurricane Relief App is an all-in-one app for requesting, providing, and coordinating help in hurricane-affected areas. It connects victims, volunteers, and shelters so help gets to the people who need it.

## How it works

Every user signs in with a username and password. To cover everyone involved in relief efforts, users are split into five roles.

### <img src="documents/icons/user-round.svg" width="20" alt=""> Victims

Victims are people whose safety, property, or lives are threatened by a hurricane. They can use the app to **request** help in several categories by giving their address, how badly they've been affected, and their contact information.

### <img src="documents/icons/hand-helping.svg" width="20" alt=""> Volunteers

Volunteers provide help to victims. Before accepting a request, a volunteer can see the victim's general location, impact level, and what kind of help they need. Once they accept, they get the victim's full details so they can assist them.

Volunteers are verified and background checked. Victims are not.

> Volunteers can also add credentials like CPR or First Aid training to their profile. Once verified, these help Dispatchers decide who to send where.

### <img src="documents/icons/radio-tower.svg" width="20" alt=""> Dispatchers

Dispatchers assign volunteers and first responders to victims' help requests. They're also responsible for organizing requests, verifying them, and following up with victims for more information.

### <img src="documents/icons/house.svg" width="20" alt=""> Shelter Workers

Shelter Workers keep shelter status up to date. They can update resource levels, transfer resources to other shelters, and answer questions about their shelter.

### <img src="documents/icons/shield-check.svg" width="20" alt=""> Admins

Admins run the app. They manage shelters, push alerts to users, and create and manage hurricanes. Admins have the highest level of verification.

## <img src="documents/icons/folder-tree.svg" width="22" alt=""> Repository layout

- `hurricane_system/` - the JavaFX application (Maven project)
- `json/` - data files for users, requests, shelters, and hurricanes
- `documents/` - design documents and diagrams

## <img src="documents/icons/file-text.svg" width="22" alt=""> Requirements

- [Design Document](documents/requirements.pdf)
- [Requirements Spreadsheet](documents/requirements_spreadsheet.pdf)

## <img src="documents/icons/chart-column-stacked.svg" width="22" alt=""> Code Design
- [UML Class Diagram](documents/uml-class-diagram.pdf)
- [UML Sequence Diagram 1](documents/uml-sequence-diagram1.pdf)
- [UML Sequence Diagram 2](documents/uml-sequence-diagram2.pdf)

## <img src="documents/icons/square-kanban.svg" width="22" alt=""> Project Board
https://github.com/users/IonizedBanana/projects/2

> Icons were provided by Lucide Icons at [lucide.dev](https://lucide.dev/).