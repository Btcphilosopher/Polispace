package com.example.data

object SampleDataset {
    val properties = listOf(
        Property(
            id = "prop_wawa_center",
            title = LocalizedText(
                pl = "Luksusowy Penthouse Śródmieście Sky",
                en = "Luxury Śródmieście Sky Penthouse"
            ),
            description = LocalizedText(
                pl = "Spektakularny penthouse położony na 42. piętrze z panoramicznym widokiem na Warszawę. Wyposażony w inteligentny system zarządzania mediami, klimatyzację, prywatną windę i luksusowe marmurowe wykończenie.",
                en = "Spectacular penthouse located on the 42nd floor with panoramic views of Warsaw. Equipped with smart utility management, air conditioning, private elevator, and luxurious marble finishing."
            ),
            address = LocalizedText(
                pl = "Złota 44, Warszawa",
                en = "Złota 44, Warsaw"
            ),
            city = "Warszawa",
            district = LocalizedText(pl = "Śródmieście", en = "City Center"),
            price = 4950000.0,
            areaSqm = 142.0,
            rooms = 4,
            floor = 42,
            maxFloors = 44,
            buildYear = 2021,
            propertyType = PropertyType.APARTMENT,
            marketType = MarketType.BUY,
            energyClass = EnergyClass.A,
            energyPerformance = 52.5,
            isFurnished = true,
            agentName = "Karol Wiśniewski",
            agentPhone = "+48 601 234 567",
            agentEmail = "k.wisniewski@polispace.pl",
            latitude = 52.2312,
            longitude = 21.0015,
            crimeRateScore = 2.1,
            transportAccessScore = 9.8,
            schoolRatingScore = 8.5,
            infrastructureZone = LocalizedText(
                pl = "Strefa Wysokiego Rozwoju (Metro, Biznes)",
                en = "High Development Zone (Metro, Business)"
            )
        ),
        Property(
            id = "prop_krak_old",
            title = LocalizedText(
                pl = "Zabytkowe Studio przy Rynku Głównym",
                en = "Historic Studio near the Main Square"
            ),
            description = LocalizedText(
                pl = "Przepiękne, klimatyczne studio w odrestaurowanej kamienicy z XIX wieku. Wysokie sufity (3.8m), zachowany oryginalny parkiet dębowy oraz nowoczesny aneks kuchenny.",
                en = "Beautiful, atmospheric studio in a restored 19th-century tenement house. High ceilings (3.8m), preserved original oak parquet, and a modern kitchenette."
            ),
            address = LocalizedText(
                pl = "Floriańska 12, Kraków",
                en = "Floriańska 12, Krakow"
            ),
            city = "Kraków",
            district = LocalizedText(pl = "Stare Miasto", en = "Old Town"),
            price = 3200.0,
            monthlyRent = 450.0,
            areaSqm = 38.0,
            rooms = 1,
            floor = 2,
            maxFloors = 3,
            buildYear = 1895,
            propertyType = PropertyType.STUDIO,
            marketType = MarketType.RENT,
            energyClass = EnergyClass.D,
            energyPerformance = 112.0,
            isFurnished = true,
            agentName = "Agnieszka Kowalska",
            agentPhone = "+48 502 987 654",
            agentEmail = "a.kowalska@polispace.pl",
            latitude = 50.0647,
            longitude = 19.9392,
            crimeRateScore = 3.5,
            transportAccessScore = 8.2,
            schoolRatingScore = 7.9,
            infrastructureZone = LocalizedText(
                pl = "Strefa Turystyczno-Kulturowa (Ochrona Zabytków)",
                en = "Tourist-Cultural Zone (Heritage Protection)"
            )
        ),
        Property(
            id = "prop_gdansk_sea",
            title = LocalizedText(
                pl = "Apartament z Widokiem na Morze",
                en = "Baltic Sea View Apartment"
            ),
            description = LocalizedText(
                pl = "Nowoczesny apartament zlokalizowany zaledwie 300 metrów od plaży w Jelitkowie. Panoramiczne okna, przestronny taras (18m²) oraz podgrzewana podłoga.",
                en = "Modern apartment located just 300 meters from the beach in Jelitkowo. Panoramic windows, spacious terrace (18m²), and underfloor heating."
            ),
            address = LocalizedText(
                pl = "Piastowska 88, Gdańsk",
                en = "Piastowska 88, Gdansk"
            ),
            city = "Gdańsk",
            district = LocalizedText(pl = "Jelitkowo", en = "Jelitkowo"),
            price = 1980000.0,
            areaSqm = 74.0,
            rooms = 3,
            floor = 5,
            maxFloors = 6,
            buildYear = 2023,
            propertyType = PropertyType.APARTMENT,
            marketType = MarketType.BUY,
            energyClass = EnergyClass.A,
            energyPerformance = 48.0,
            isFurnished = false,
            agentName = "Maciej Bałtycki",
            agentPhone = "+48 58 712 34 56",
            agentEmail = "m.baltycki@polispace.pl",
            latitude = 54.4239,
            longitude = 18.5991,
            crimeRateScore = 1.5,
            transportAccessScore = 7.1,
            schoolRatingScore = 8.0,
            infrastructureZone = LocalizedText(
                pl = "Nadmorski Pas Rekreacyjny",
                en = "Coastal Recreational Zone"
            )
        ),
        Property(
            id = "prop_wroc_house",
            title = LocalizedText(
                pl = "Ekologiczny Dom Pasywny Krzyki",
                en = "Eco Passive House in Krzyki"
            ),
            description = LocalizedText(
                pl = "Willa miejska w zabudowie bliźniaczej, zbudowana w standardzie pasywnym. Pompa ciepła, fotowoltaika 8kW, rekuperacja oraz własne ujęcie wody.",
                en = "Twin villa constructed to strict passive-house standards. Heat pump, 8kW photovoltaic setup, heat recovery ventilation, and private well water system."
            ),
            address = LocalizedText(
                pl = "Skarbowców 32, Wrocław",
                en = "Skarbowców 32, Wroclaw"
            ),
            city = "Wrocław",
            district = LocalizedText(pl = "Krzyki", en = "Krzyki"),
            price = 2450000.0,
            areaSqm = 185.0,
            rooms = 5,
            floor = 0,
            maxFloors = 2,
            buildYear = 2024,
            propertyType = PropertyType.HOUSE,
            marketType = MarketType.BUY,
            energyClass = EnergyClass.A,
            energyPerformance = 15.2,
            isFurnished = true,
            agentName = "Ewa Wrocławska",
            agentPhone = "+48 71 345 67 89",
            agentEmail = "e.wroclawska@polispace.pl",
            latitude = 51.0743,
            longitude = 17.0123,
            crimeRateScore = 1.8,
            transportAccessScore = 7.5,
            schoolRatingScore = 9.0,
            infrastructureZone = LocalizedText(
                pl = "Zielone Przedmieście Miejskie",
                en = "Green Urban Suburban Zone"
            )
        ),
        Property(
            id = "prop_poznan_comm",
            title = LocalizedText(
                pl = "Siedziba Kreatywna Jeżyce",
                en = "Jeżyce Creative Corporate Headquarters"
            ),
            description = LocalizedText(
                pl = "Industrialna przestrzeń komercyjna idealna na biuro projektowe, agencję reklamową lub kancelarię. Duże witryny sklepowe i bezpośrednie wejście z ulicy.",
                en = "Industrial commercial space perfect for a design office, advertising agency, or law firm. Large storefront windows and direct street entry."
            ),
            address = LocalizedText(
                pl = "Dąbrowskiego 54, Poznań",
                en = "Dąbrowskiego 54, Poznan"
            ),
            city = "Poznań",
            district = LocalizedText(pl = "Jeżyce", en = "Jezyce"),
            price = 1200000.0,
            areaSqm = 95.0,
            rooms = 3,
            floor = 0,
            maxFloors = 4,
            buildYear = 1910,
            propertyType = PropertyType.COMMERCIAL,
            marketType = MarketType.BUY,
            energyClass = EnergyClass.C,
            energyPerformance = 88.0,
            isFurnished = false,
            agentName = "Tomasz Rogal",
            agentPhone = "+48 61 876 54 32",
            agentEmail = "t.rogal@polispace.pl",
            latitude = 52.4111,
            longitude = 16.9022,
            crimeRateScore = 2.9,
            transportAccessScore = 9.1,
            schoolRatingScore = 6.8,
            infrastructureZone = LocalizedText(
                pl = "Strefa Rewitalizacji Miejskiej",
                en = "Urban Revitalization Zone"
            )
        ),
        Property(
            id = "prop_wawa_moko",
            title = LocalizedText(
                pl = "Designerski Loft z Tarasem",
                en = "Designer Loft with Rooftop Terrace"
            ),
            description = LocalizedText(
                pl = "Niezwykły, dwupoziomowy loft wykończony surowym betonem i cegłą. Położony w sercu biznesowego Mokotowa. Wyposażony w system audio Sonos oraz kino domowe.",
                en = "Exceptional double-level loft finished with raw concrete and brickwork. Located in the heart of business Mokotów district. Fitted with Sonos sound systems and home cinema."
            ),
            address = LocalizedText(
                pl = "Woronicza 15, Warszawa",
                en = "Woronicza 15, Warsaw"
            ),
            city = "Warszawa",
            district = LocalizedText(pl = "Mokotów", en = "Mokotow"),
            price = 4800.0,
            monthlyRent = 650.0,
            areaSqm = 65.0,
            rooms = 2,
            floor = 3,
            maxFloors = 5,
            buildYear = 2018,
            propertyType = PropertyType.APARTMENT,
            marketType = MarketType.RENT,
            energyClass = EnergyClass.B,
            energyPerformance = 64.2,
            isFurnished = true,
            agentName = "Karol Wiśniewski",
            agentPhone = "+48 601 234 567",
            agentEmail = "k.wisniewski@polispace.pl",
            latitude = 52.1884,
            longitude = 21.0118,
            crimeRateScore = 1.9,
            transportAccessScore = 8.8,
            schoolRatingScore = 8.2,
            infrastructureZone = LocalizedText(
                pl = "Nowoczesna Dzielnica Biznesowa",
                en = "Modern Business District"
            )
        )
    )
}
