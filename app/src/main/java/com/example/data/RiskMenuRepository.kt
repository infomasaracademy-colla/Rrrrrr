package com.example.data

object RiskMenuRepository {

    val restaurant = RestaurantContact()

    val availableExtras = listOf(
        ExtraOption("ext_mozzarella", "إضافة جبنة موتزاريلا", 20.0),
        ExtraOption("ext_roumi", "إضافة جبنة رومي", 20.0),
        ExtraOption("ext_cheddar", "إضافة جبنة شيدر", 15.0),
        ExtraOption("ext_kiri", "إضافة جبنة كيري", 25.0),
        ExtraOption("ext_meat", "إضافة لحوم مشكلة", 40.0),
        ExtraOption("ext_fries_packet", "باكت بطاطس فارم فريتس", 30.0)
    )

    val availableSauces = listOf(
        SauceOption("sauce_bbq", "صوص باربكيو", 10.0),
        SauceOption("sauce_ranch", "صوص رانش كريمي", 20.0),
        SauceOption("sauce_cheddar", "صوص جبنة شيدر سايحة", 20.0),
        SauceOption("sauce_sweet_chili", "صوص سويت شيلي", 20.0),
        SauceOption("sauce_mayo", "مايونيز فاخر", 10.0),
        SauceOption("sauce_hot", "هوت صوص حار", 10.0),
        SauceOption("sauce_ketchup", "كاتشب", 10.0),
        SauceOption("sauce_tomia", "تومية سورية أصيلة", 10.0)
    )

    val menuItems: List<MenuItem> = buildList {
        // --- 1. البيتزا إيطالي ---
        add(
            MenuItem(
                id = "p_margherita",
                nameAr = "بيتزا مارجريتا",
                category = MenuCategory.PIZZA,
                descriptionAr = "صلصة طماطم إيطالية خاصة، جبنة موتزاريلا طبيعية وزعتر بري",
                sizePrices = mapOf(ItemSize.SMALL to 80.0, ItemSize.MEDIUM to 100.0, ItemSize.LARGE to 120.0),
                allowsStuffedCrust = true
            )
        )
        add(
            MenuItem(
                id = "p_mix_cheese",
                nameAr = "بيتزا ميكس جبن",
                category = MenuCategory.PIZZA,
                descriptionAr = "توليفة غنية من موتزاريلا، شيدر، ورومي مبشور فاخر",
                sizePrices = mapOf(ItemSize.SMALL to 100.0, ItemSize.MEDIUM to 120.0, ItemSize.LARGE to 140.0),
                allowsStuffedCrust = true
            )
        )
        add(
            MenuItem(
                id = "p_meat",
                nameAr = "بيتزا لحمة",
                category = MenuCategory.PIZZA,
                descriptionAr = "لحم مفروم بلدي متبل مع صلصة إيطالية وخضار طازج",
                sizePrices = mapOf(ItemSize.SMALL to 130.0, ItemSize.MEDIUM to 160.0, ItemSize.LARGE to 190.0),
                allowsStuffedCrust = true
            )
        )
        add(
            MenuItem(
                id = "p_hotdog",
                nameAr = "بيتزا هوت دوج",
                category = MenuCategory.PIZZA,
                descriptionAr = "قطع هوت دوج مدخن مع فلفل وزيتون وموتزاريلا شهية",
                sizePrices = mapOf(ItemSize.SMALL to 100.0, ItemSize.MEDIUM to 120.0, ItemSize.LARGE to 140.0),
                allowsStuffedCrust = true
            )
        )
        add(
            MenuItem(
                id = "p_tuna",
                nameAr = "بيتزا تونة",
                category = MenuCategory.PIZZA,
                descriptionAr = "تونة قطع فاخرة، بصل، فلفل أخضر، زيتون وموتزاريلا",
                sizePrices = mapOf(ItemSize.SMALL to 100.0, ItemSize.MEDIUM to 130.0, ItemSize.LARGE to 160.0),
                allowsStuffedCrust = true
            )
        )
        add(
            MenuItem(
                id = "p_chicken",
                nameAr = "بيتزا فراخ",
                category = MenuCategory.PIZZA,
                descriptionAr = "صدور دجاج متبلة مشوية مع فلفل ألوان وزيتون أسود",
                sizePrices = mapOf(ItemSize.SMALL to 130.0, ItemSize.MEDIUM to 160.0, ItemSize.LARGE to 190.0),
                allowsStuffedCrust = true
            )
        )
        add(
            MenuItem(
                id = "p_strips",
                nameAr = "بيتزا ستربس",
                category = MenuCategory.PIZZA,
                descriptionAr = "قطع دجاج استربس مقرمشة ذهبية مع صوص الجبن والزيتون",
                sizePrices = mapOf(ItemSize.SMALL to 120.0, ItemSize.MEDIUM to 150.0, ItemSize.LARGE to 170.0),
                allowsStuffedCrust = true
            )
        )
        add(
            MenuItem(
                id = "p_zinger",
                nameAr = "بيتزا زنجر",
                category = MenuCategory.PIZZA,
                descriptionAr = "دجاج زنجر حار ومقرمش مع موتزاريلا وصوصات مميزة",
                sizePrices = mapOf(ItemSize.SMALL to 120.0, ItemSize.MEDIUM to 150.0, ItemSize.LARGE to 170.0),
                allowsStuffedCrust = true
            )
        )
        add(
            MenuItem(
                id = "p_mix_chicken",
                nameAr = "بيتزا ميكس فراخ",
                category = MenuCategory.PIZZA,
                descriptionAr = "مزيج صدور دجاج، زنجر واستربس مقرمش لعشاق الدجاج",
                sizePrices = mapOf(ItemSize.SMALL to 130.0, ItemSize.MEDIUM to 160.0, ItemSize.LARGE to 200.0),
                allowsStuffedCrust = true
            )
        )
        add(
            MenuItem(
                id = "p_oriental_sausage",
                nameAr = "بيتزا سجق",
                category = MenuCategory.PIZZA,
                descriptionAr = "سجق شرقي بلدي بالخلطة الخاصة مع فلفل وطماطم وموتزاريلا",
                sizePrices = mapOf(ItemSize.SMALL to 120.0, ItemSize.MEDIUM to 150.0, ItemSize.LARGE to 190.0),
                allowsStuffedCrust = true
            )
        )
        add(
            MenuItem(
                id = "p_mix_meat",
                nameAr = "بيتزا ميكس لحوم",
                category = MenuCategory.PIZZA,
                descriptionAr = "تشكيلة من اللحم المفروم، السجق، والهوت دوج اللذيذ",
                sizePrices = mapOf(ItemSize.SMALL to 140.0, ItemSize.MEDIUM to 170.0, ItemSize.LARGE to 210.0),
                allowsStuffedCrust = true
            )
        )
        add(
            MenuItem(
                id = "p_sosis",
                nameAr = "بيتزا سوسيس",
                category = MenuCategory.PIZZA,
                descriptionAr = "قطع سوسيس مشوية مع خضار مشكل وموتزاريلا غنية",
                sizePrices = mapOf(ItemSize.SMALL to 100.0, ItemSize.MEDIUM to 120.0, ItemSize.LARGE to 140.0),
                allowsStuffedCrust = true
            )
        )
        add(
            MenuItem(
                id = "p_pane",
                nameAr = "بيتزا بانيه",
                category = MenuCategory.PIZZA,
                descriptionAr = "قطع دجاج بانيه متبلة ومقرمشة مع طبقة موتزاريلا",
                sizePrices = mapOf(ItemSize.SMALL to 80.0, ItemSize.MEDIUM to 100.0, ItemSize.LARGE to 120.0),
                allowsStuffedCrust = true
            )
        )
        add(
            MenuItem(
                id = "p_risk_special",
                nameAr = "بيتزا ريسك الخاصة",
                category = MenuCategory.PIZZA,
                descriptionAr = "الصنف الملكي المميز! ميكس لحوم ودجاج مع إكسترا جبن وصوص ريسك الخاص",
                sizePrices = mapOf(ItemSize.SMALL to 170.0, ItemSize.MEDIUM to 220.0, ItemSize.LARGE to 270.0),
                isRiskSpecial = true,
                allowsStuffedCrust = true
            )
        )

        // --- 2. فطير شرقي ---
        add(
            MenuItem(
                id = "fo_mix_cheese",
                nameAr = "فطيرة مشكل جبن شرقي",
                category = MenuCategory.FETEER_ORIENTAL,
                descriptionAr = "فطير مورق بالسمن البلدي محشو بموتزاريلا وشيدر ورومي",
                sizePrices = mapOf(ItemSize.SMALL to 130.0, ItemSize.MEDIUM to 155.0, ItemSize.LARGE to 180.0)
            )
        )
        add(
            MenuItem(
                id = "fo_chicken",
                nameAr = "فطيرة فراخ شرقي",
                category = MenuCategory.FETEER_ORIENTAL,
                descriptionAr = "فطير شرقي محشو صدور فراخ متبلة مع فلفل وطماطم وزيتون",
                sizePrices = mapOf(ItemSize.SMALL to 160.0, ItemSize.MEDIUM to 180.0, ItemSize.LARGE to 200.0)
            )
        )
        add(
            MenuItem(
                id = "fo_strips",
                nameAr = "فطيرة ستربس شرقي",
                category = MenuCategory.FETEER_ORIENTAL,
                descriptionAr = "حشوة استربس كرسبي مع صوصات وجبن مميزة داخل العجين المورق",
                sizePrices = mapOf(ItemSize.SMALL to 150.0, ItemSize.MEDIUM to 175.0, ItemSize.LARGE to 200.0)
            )
        )
        add(
            MenuItem(
                id = "fo_zinger",
                nameAr = "فطيرة زنجر شرقي",
                category = MenuCategory.FETEER_ORIENTAL,
                descriptionAr = "فطيرة شرقية حارة بحشوة دجاج زنجر المقرمش والجبن السايحة",
                sizePrices = mapOf(ItemSize.SMALL to 150.0, ItemSize.MEDIUM to 175.0, ItemSize.LARGE to 200.0)
            )
        )
        add(
            MenuItem(
                id = "fo_mix_chicken",
                nameAr = "فطيرة ميكس فراخ شرقي",
                category = MenuCategory.FETEER_ORIENTAL,
                descriptionAr = "ميكس دجاج متكامل (صدور، زنجر، استربس) داخل فطير شرقي شهي",
                sizePrices = mapOf(ItemSize.SMALL to 190.0, ItemSize.MEDIUM to 210.0, ItemSize.LARGE to 230.0)
            )
        )
        add(
            MenuItem(
                id = "fo_minced_meat",
                nameAr = "فطيرة لحمة مفرومة شرقي",
                category = MenuCategory.FETEER_ORIENTAL,
                descriptionAr = "لحم مفروم بلدي بالبصل والبهارات الشرقية والجبن",
                sizePrices = mapOf(ItemSize.SMALL to 160.0, ItemSize.MEDIUM to 180.0, ItemSize.LARGE to 200.0)
            )
        )
        add(
            MenuItem(
                id = "fo_sosis",
                nameAr = "فطيرة سوسيس شرقي",
                category = MenuCategory.FETEER_ORIENTAL,
                descriptionAr = "فطير شرقي محشو سوسيس شهي وخضروات طازجة",
                sizePrices = mapOf(ItemSize.SMALL to 140.0, ItemSize.MEDIUM to 160.0, ItemSize.LARGE to 180.0)
            )
        )
        add(
            MenuItem(
                id = "fo_hotdog",
                nameAr = "فطيرة هوت دوج شرقي",
                category = MenuCategory.FETEER_ORIENTAL,
                descriptionAr = "حشوة هوت دوج مدخن مع جبنة رومي وموتزاريلا",
                sizePrices = mapOf(ItemSize.SMALL to 140.0, ItemSize.MEDIUM to 160.0, ItemSize.LARGE to 180.0)
            )
        )
        add(
            MenuItem(
                id = "fo_sausage",
                nameAr = "فطيرة سجق شرقي",
                category = MenuCategory.FETEER_ORIENTAL,
                descriptionAr = "سجق بلدي متبل بخلطة قنا الشرقية الأصيلة",
                sizePrices = mapOf(ItemSize.SMALL to 160.0, ItemSize.MEDIUM to 195.0, ItemSize.LARGE to 240.0)
            )
        )
        add(
            MenuItem(
                id = "fo_mix_meat",
                nameAr = "فطيرة ميكس لحوم شرقي",
                category = MenuCategory.FETEER_ORIENTAL,
                descriptionAr = "لحمة مفرومة + سجق + هوت دوج في فطيرة دسمة ولذيذة",
                sizePrices = mapOf(ItemSize.SMALL to 190.0, ItemSize.MEDIUM to 210.0, ItemSize.LARGE to 230.0)
            )
        )
        add(
            MenuItem(
                id = "fo_tuna",
                nameAr = "فطيرة تونة شرقي",
                category = MenuCategory.FETEER_ORIENTAL,
                descriptionAr = "تونة فاخرة مع بصل وفلفل وزيتون وموتزاريلا",
                sizePrices = mapOf(ItemSize.SMALL to 140.0, ItemSize.MEDIUM to 170.0, ItemSize.LARGE to 210.0)
            )
        )
        add(
            MenuItem(
                id = "fo_risk_special",
                nameAr = "فطيرة ريسك الخاصة (سوبر)",
                category = MenuCategory.FETEER_ORIENTAL,
                descriptionAr = "فطيرة ريسك العملاقة بحشوة مزدوجة لحوم وفراخ وأجبان خاصة",
                sizePrices = mapOf(ItemSize.SMALL to 220.0, ItemSize.MEDIUM to 290.0, ItemSize.LARGE to 360.0),
                isRiskSpecial = true
            )
        )

        // --- 3. فطير حادق ---
        add(
            MenuItem(
                id = "fs_mix_cheese",
                nameAr = "فطيرة حادق مشكل جبن",
                category = MenuCategory.FETEER_SALTY,
                descriptionAr = "فطيرة حادقة خفيفة ومقرمشة بمكس أجبان متنوعة",
                sizePrices = mapOf(ItemSize.SMALL to 120.0, ItemSize.MEDIUM to 140.0, ItemSize.LARGE to 160.0)
            )
        )
        add(
            MenuItem(
                id = "fs_chicken",
                nameAr = "فطيرة حادق فراخ",
                category = MenuCategory.FETEER_SALTY,
                descriptionAr = "فراخ متبلة مع خضروات وجبن حادق لذيذ",
                sizePrices = mapOf(ItemSize.SMALL to 150.0, ItemSize.MEDIUM to 170.0, ItemSize.LARGE to 200.0)
            )
        )
        add(
            MenuItem(
                id = "fs_strips",
                nameAr = "فطيرة حادق ستربس",
                category = MenuCategory.FETEER_SALTY,
                descriptionAr = "قطع استربس ذهبية مع جبن وخضار",
                sizePrices = mapOf(ItemSize.SMALL to 140.0, ItemSize.MEDIUM to 160.0, ItemSize.LARGE to 180.0)
            )
        )
        add(
            MenuItem(
                id = "fs_zinger",
                nameAr = "فطيرة حادق زنجر",
                category = MenuCategory.FETEER_SALTY,
                descriptionAr = "زنجر كرانشي حار وجبن وفلفل",
                sizePrices = mapOf(ItemSize.SMALL to 140.0, ItemSize.MEDIUM to 160.0, ItemSize.LARGE to 180.0)
            )
        )
        add(
            MenuItem(
                id = "fs_mix_chicken",
                nameAr = "فطيرة حادق ميكس فراخ",
                category = MenuCategory.FETEER_SALTY,
                descriptionAr = "مكس أنواع الدجاج المفضلة بخلطة حادقة",
                sizePrices = mapOf(ItemSize.SMALL to 180.0, ItemSize.MEDIUM to 200.0, ItemSize.LARGE to 220.0)
            )
        )
        add(
            MenuItem(
                id = "fs_minced_meat",
                nameAr = "فطيرة حادق لحمة مفرومة",
                category = MenuCategory.FETEER_SALTY,
                descriptionAr = "لحم مفروم مع تتبيلة شرقية وجبن",
                sizePrices = mapOf(ItemSize.SMALL to 150.0, ItemSize.MEDIUM to 180.0, ItemSize.LARGE to 210.0)
            )
        )
        add(
            MenuItem(
                id = "fs_sosis",
                nameAr = "فطيرة حادق سوسيس",
                category = MenuCategory.FETEER_SALTY,
                descriptionAr = "سوسيس متبل مع صوص وجبن",
                sizePrices = mapOf(ItemSize.SMALL to 120.0, ItemSize.MEDIUM to 140.0, ItemSize.LARGE to 160.0)
            )
        )
        add(
            MenuItem(
                id = "fs_hotdog",
                nameAr = "فطيرة حادق هوت دوج",
                category = MenuCategory.FETEER_SALTY,
                descriptionAr = "هوت دوج وجبن وزيتون وفلفل",
                sizePrices = mapOf(ItemSize.SMALL to 120.0, ItemSize.MEDIUM to 140.0, ItemSize.LARGE to 160.0)
            )
        )
        add(
            MenuItem(
                id = "fs_sausage",
                nameAr = "فطيرة حادق سجق",
                category = MenuCategory.FETEER_SALTY,
                descriptionAr = "سجق بلدي خلطة سرية مع بهارات غنية",
                sizePrices = mapOf(ItemSize.SMALL to 150.0, ItemSize.MEDIUM to 180.0, ItemSize.LARGE to 220.0)
            )
        )
        add(
            MenuItem(
                id = "fs_mix_meat",
                nameAr = "فطيرة حادق ميكس لحوم",
                category = MenuCategory.FETEER_SALTY,
                descriptionAr = "ميكس لحوم غني ومشبع جداً",
                sizePrices = mapOf(ItemSize.SMALL to 180.0, ItemSize.MEDIUM to 200.0, ItemSize.LARGE to 220.0)
            )
        )
        add(
            MenuItem(
                id = "fs_tuna",
                nameAr = "فطيرة حادق تونة",
                category = MenuCategory.FETEER_SALTY,
                descriptionAr = "تونة مع ليمون وخضار وجبن",
                sizePrices = mapOf(ItemSize.SMALL to 120.0, ItemSize.MEDIUM to 150.0, ItemSize.LARGE to 190.0)
            )
        )
        add(
            MenuItem(
                id = "fs_risk_special",
                nameAr = "فطيرة حادق ريسك",
                category = MenuCategory.FETEER_SALTY,
                descriptionAr = "فطيرة ريسك الحادقة المميزة بالخلطة السرية",
                sizePrices = mapOf(ItemSize.SMALL to 200.0, ItemSize.MEDIUM to 250.0, ItemSize.LARGE to 300.0),
                isRiskSpecial = true
            )
        )

        // --- 4. فطائر حلو ---
        add(
            MenuItem(
                id = "fsw_sugar",
                nameAr = "فطيرة سكر",
                category = MenuCategory.FETEER_SWEET,
                descriptionAr = "فطيرة مورقة بالسمن البلدي والسكر البودرة والحليب الساخن",
                priceSingle = 35.0
            )
        )
        add(
            MenuItem(
                id = "fsw_baghasha",
                nameAr = "فطيرة بغاشة",
                category = MenuCategory.FETEER_SWEET,
                descriptionAr = "فطيرة بغاشة بالسمن البلدي والسيرب اللذيذ",
                priceSingle = 55.0
            )
        )
        add(
            MenuItem(
                id = "fsw_custard",
                nameAr = "فطيرة كاستر",
                category = MenuCategory.FETEER_SWEET,
                descriptionAr = "كاستر غني كريمي داخل الفطير الساخن وسكر بودرة",
                priceSingle = 70.0
            )
        )
        add(
            MenuItem(
                id = "fsw_cream_honey",
                nameAr = "فطيرة قشطة وعسل",
                category = MenuCategory.FETEER_SWEET,
                descriptionAr = "قشطة بلدي طبيعية مع عسل النحل الصافي",
                priceSingle = 80.0
            )
        )
        add(
            MenuItem(
                id = "fsw_nutella",
                nameAr = "فطيرة نوتيلا",
                category = MenuCategory.FETEER_SWEET,
                descriptionAr = "شوكولاتة نوتيلا أصلية سايحة وغنية",
                priceSingle = 70.0
            )
        )
        add(
            MenuItem(
                id = "fsw_cream_nuts",
                nameAr = "فطيرة قشطة مكسرات",
                category = MenuCategory.FETEER_SWEET,
                descriptionAr = "قشطة بلدي مع مكسرات محمصة مقرمشة وعسل",
                priceSingle = 80.0
            )
        )
        add(
            MenuItem(
                id = "fsw_nutella_nuts",
                nameAr = "فطيرة نوتيلا مكسرات",
                category = MenuCategory.FETEER_SWEET,
                descriptionAr = "شوكولاتة نوتيلا غنية مع تشكيلة مكسرات فاخرة",
                priceSingle = 85.0
            )
        )
        add(
            MenuItem(
                id = "fsw_kings",
                nameAr = "فطيرة الملوك (الملكية)",
                category = MenuCategory.FETEER_SWEET,
                descriptionAr = "قشطة + نوتيلا + عسل + مكسرات محمصة + كاستر! تجربة ملكية لا تُنسى",
                priceSingle = 150.0,
                isRiskSpecial = true
            )
        )

        // --- 5. الصاروخ ---
        add(
            MenuItem(
                id = "sr_chicken",
                nameAr = "صاروخ فراخ",
                category = MenuCategory.SAROUKH,
                descriptionAr = "ساندوتش صاروخ عملاق محشو دجاج مشوي متبل مع صوصات وجبن",
                priceSingle = 120.0
            )
        )
        add(
            MenuItem(
                id = "sr_meat",
                nameAr = "صاروخ لحمة مفرومة",
                category = MenuCategory.SAROUKH,
                descriptionAr = "صاروخ محشو لحم مفروم بلدي متبل مع صوص الجبن والبطاطس",
                priceSingle = 130.0
            )
        )
        add(
            MenuItem(
                id = "sr_mix_cheese",
                nameAr = "صاروخ مشكل جبن",
                category = MenuCategory.SAROUKH,
                descriptionAr = "صاروخ محشو شيدر وموتزاريلا ورومي وكيري سايحة",
                priceSingle = 100.0
            )
        )
        add(
            MenuItem(
                id = "sr_mix_meat",
                nameAr = "صاروخ مشكل لحوم",
                category = MenuCategory.SAROUKH,
                descriptionAr = "مزيج لحوم بلدي، سجق وهووت دوج مع صوصات غنية",
                priceSingle = 150.0
            )
        )
        add(
            MenuItem(
                id = "sr_mix_chicken",
                nameAr = "صاروخ مشكل فراخ",
                category = MenuCategory.SAROUKH,
                descriptionAr = "صدور دجاج + استربس + زنجر في ساندوتش صاروخ ضخم",
                priceSingle = 140.0
            )
        )
        add(
            MenuItem(
                id = "sr_sausage",
                nameAr = "صاروخ سجق",
                category = MenuCategory.SAROUKH,
                descriptionAr = "سجق شرقي بخلطة ريسك الخاصة والجبن",
                priceSingle = 130.0
            )
        )
        add(
            MenuItem(
                id = "sr_hotdog",
                nameAr = "صاروخ هوت دوج",
                category = MenuCategory.SAROUKH,
                descriptionAr = "هوت دوج مدخن مع ميكس جبن وصوصات",
                priceSingle = 80.0
            )
        )
        add(
            MenuItem(
                id = "sr_tuna",
                nameAr = "صاروخ تونة",
                category = MenuCategory.SAROUKH,
                descriptionAr = "تونة قطع مع خضار وصوص تومية ومايونيز",
                priceSingle = 120.0
            )
        )
        add(
            MenuItem(
                id = "sr_sosis",
                nameAr = "صاروخ سوسيس",
                category = MenuCategory.SAROUKH,
                descriptionAr = "سوسيس مشوي مع صوص شيدر وخضار",
                priceSingle = 80.0
            )
        )
        add(
            MenuItem(
                id = "sr_strips",
                nameAr = "صاروخ ستربس",
                category = MenuCategory.SAROUKH,
                descriptionAr = "استربس دجاج مقرمش مع صوص رانش وشيدر",
                priceSingle = 110.0
            )
        )
        add(
            MenuItem(
                id = "sr_zinger",
                nameAr = "صاروخ زنجر",
                category = MenuCategory.SAROUKH,
                descriptionAr = "زنجر مقرمش سبايسي مع هالبينو وجبن",
                priceSingle = 110.0
            )
        )
        add(
            MenuItem(
                id = "sr_risk_special",
                nameAr = "صاروخ ريسك سبيشال",
                category = MenuCategory.SAROUKH,
                descriptionAr = "الصاروخ الأكثر طلباً! خلطة ريسك الملكية الكاملة",
                priceSingle = 160.0,
                isRiskSpecial = true
            )
        )

        // --- 6. الحواوشي الإيطالي ---
        add(
            MenuItem(
                id = "hi_mix_cheese",
                nameAr = "حواوشي إيطالي ميكس جبن",
                category = MenuCategory.HAWAWSHI_ITALIAN,
                descriptionAr = "عجينة إيطالية مقرمشة محشوة بتشكيلة أجبان غنية",
                sizePrices = mapOf(ItemSize.MEDIUM to 45.0, ItemSize.LARGE to 65.0)
            )
        )
        add(
            MenuItem(
                id = "hi_chicken",
                nameAr = "حواوشي إيطالي فراخ",
                category = MenuCategory.HAWAWSHI_ITALIAN,
                descriptionAr = "فراخ متبلة مع موزاريلا وزيتون في عجين إيطالي مخبوز",
                sizePrices = mapOf(ItemSize.MEDIUM to 60.0, ItemSize.LARGE to 80.0)
            )
        )
        add(
            MenuItem(
                id = "hi_meat",
                nameAr = "حواوشي إيطالي لحمة",
                category = MenuCategory.HAWAWSHI_ITALIAN,
                descriptionAr = "لحم بلدي متبل بنكهة إيطالية مع الموتزاريلا",
                sizePrices = mapOf(ItemSize.MEDIUM to 70.0, ItemSize.LARGE to 90.0)
            )
        )
        add(
            MenuItem(
                id = "hi_sausage",
                nameAr = "حواوشي إيطالي سجق",
                category = MenuCategory.HAWAWSHI_ITALIAN,
                descriptionAr = "سجق إيطالي متبل مع صوص طماطم وأجبان",
                sizePrices = mapOf(ItemSize.MEDIUM to 70.0, ItemSize.LARGE to 100.0)
            )
        )
        add(
            MenuItem(
                id = "hi_sosis",
                nameAr = "حواوشي إيطالي سوسيس",
                category = MenuCategory.HAWAWSHI_ITALIAN,
                descriptionAr = "سوسيس مع ميكس أجبان وفلفل",
                sizePrices = mapOf(ItemSize.MEDIUM to 50.0, ItemSize.LARGE to 70.0)
            )
        )
        add(
            MenuItem(
                id = "hi_mix_chicken",
                nameAr = "حواوشي إيطالي مكس فراخ",
                category = MenuCategory.HAWAWSHI_ITALIAN,
                descriptionAr = "تشكيلة من قطع الدجاج والاستربس مع الموتزاريلا",
                sizePrices = mapOf(ItemSize.MEDIUM to 85.0, ItemSize.LARGE to 110.0)
            )
        )
        add(
            MenuItem(
                id = "hi_mix_meat",
                nameAr = "حواوشي إيطالي ميكس لحوم",
                category = MenuCategory.HAWAWSHI_ITALIAN,
                descriptionAr = "لحمة مفرومة + سجق + سوسيس مع صوص إيطالي",
                sizePrices = mapOf(ItemSize.MEDIUM to 90.0, ItemSize.LARGE to 120.0)
            )
        )
        add(
            MenuItem(
                id = "hi_hotdog",
                nameAr = "حواوشي إيطالي هوت دوج",
                category = MenuCategory.HAWAWSHI_ITALIAN,
                descriptionAr = "هوت دوج وجبن وفلفل وزيتون في عجين مقرمش",
                sizePrices = mapOf(ItemSize.MEDIUM to 50.0, ItemSize.LARGE to 70.0)
            )
        )
        add(
            MenuItem(
                id = "hi_pane",
                nameAr = "حواوشي إيطالي بانيه",
                category = MenuCategory.HAWAWSHI_ITALIAN,
                descriptionAr = "دجاج بانيه متبل ومقرمش مع جبنة إيطالية",
                sizePrices = mapOf(ItemSize.MEDIUM to 50.0, ItemSize.LARGE to 70.0)
            )
        )

        // --- 7. الحواوشي البلدي ---
        add(
            MenuItem(
                id = "hb_regular",
                nameAr = "حواوشي لحمة بلدي",
                category = MenuCategory.HAWAWSHI_BALADI,
                descriptionAr = "عيش بلدي طازج محشو لحم بلدي متبل بالبهارات واللية",
                priceSingle = 25.0
            )
        )
        add(
            MenuItem(
                id = "hb_roumi",
                nameAr = "حواوشي بلدي جبنة رومي",
                category = MenuCategory.HAWAWSHI_BALADI,
                descriptionAr = "لحم بلدي متبل مغطى بطبقة جبنة رومي قديمة",
                priceSingle = 35.0
            )
        )
        add(
            MenuItem(
                id = "hb_cheddar",
                nameAr = "حواوشي بلدي جبنة شيدر",
                category = MenuCategory.HAWAWSHI_BALADI,
                descriptionAr = "لحم بلدي مع صوص وجبنة شيدر سايحة",
                priceSingle = 35.0
            )
        )
        add(
            MenuItem(
                id = "hb_mozzarella",
                nameAr = "حواوشي بلدي موزاريلا",
                category = MenuCategory.HAWAWSHI_BALADI,
                descriptionAr = "لحم بلدي مع جبنة موتزاريلا مطاطية شهية",
                priceSingle = 35.0
            )
        )
        add(
            MenuItem(
                id = "hb_mix_cheese",
                nameAr = "حواوشي بلدي ميكس جبن",
                category = MenuCategory.HAWAWSHI_BALADI,
                descriptionAr = "لحم بلدي مع ميكس رومي وشيدر وموتزاريلا",
                priceSingle = 50.0
            )
        )

        // --- 8. السوري ---
        add(
            MenuItem(
                id = "sy_fries",
                nameAr = "ساندوتش بطاطس سوري",
                category = MenuCategory.SYRIAN,
                descriptionAr = "بطاطس فارم فريتس مع تومية ومخلل خيار في عيش صاج سوري",
                sizePrices = mapOf(ItemSize.SMALL to 10.0, ItemSize.MEDIUM to 15.0, ItemSize.LARGE to 25.0)
            )
        )
        add(
            MenuItem(
                id = "sy_cheddar",
                nameAr = "ساندوتش شيدر سوري",
                category = MenuCategory.SYRIAN,
                descriptionAr = "جبنة شيدر سايحة ومخلل وتومية في خبز صاج مقرمش",
                sizePrices = mapOf(ItemSize.MEDIUM to 20.0, ItemSize.LARGE to 30.0)
            )
        )
        add(
            MenuItem(
                id = "sy_pane",
                nameAr = "ساندوتش بانيه سوري",
                category = MenuCategory.SYRIAN,
                descriptionAr = "دجاج بانيه متبل ومقرمش مع تومية وكاتشب",
                sizePrices = mapOf(ItemSize.MEDIUM to 25.0, ItemSize.LARGE to 45.0)
            )
        )
        add(
            MenuItem(
                id = "sy_mozzarella",
                nameAr = "ساندوتش موتزاريلا سوري",
                category = MenuCategory.SYRIAN,
                descriptionAr = "موتزاريلا ذائبة مع توابل وصوصات",
                sizePrices = mapOf(ItemSize.MEDIUM to 25.0, ItemSize.LARGE to 35.0)
            )
        )
        add(
            MenuItem(
                id = "sy_roumi",
                nameAr = "ساندوتش رومي سوري",
                category = MenuCategory.SYRIAN,
                descriptionAr = "جبنة رومي مبشورة ومحمصة داخل الخبز السوري",
                sizePrices = mapOf(ItemSize.MEDIUM to 25.0, ItemSize.LARGE to 35.0)
            )
        )
        add(
            MenuItem(
                id = "sy_mix_cheese",
                nameAr = "ساندوتش مكس جبن سوري",
                category = MenuCategory.SYRIAN,
                descriptionAr = "شيدر ورومي وموتزاريلا سايحة",
                sizePrices = mapOf(ItemSize.MEDIUM to 40.0, ItemSize.LARGE to 50.0)
            )
        )
        add(
            MenuItem(
                id = "sy_burger",
                nameAr = "ساندوتش برجر سوري",
                category = MenuCategory.SYRIAN,
                descriptionAr = "برجر مشوي على الجريل مع صوص وتومية في صاج",
                sizePrices = mapOf(ItemSize.MEDIUM to 30.0, ItemSize.LARGE to 40.0)
            )
        )
        add(
            MenuItem(
                id = "sy_kofta",
                nameAr = "ساندوتش كفتة سوري",
                category = MenuCategory.SYRIAN,
                descriptionAr = "كفتة بلدي مشوية ومتبلة مع طحينة وتومية",
                sizePrices = mapOf(ItemSize.MEDIUM to 30.0, ItemSize.LARGE to 40.0)
            )
        )
        add(
            MenuItem(
                id = "sy_hotdog",
                nameAr = "ساندوتش هوت دوج سوري",
                category = MenuCategory.SYRIAN,
                descriptionAr = "هوت دوج مشوي مع صوصات سورية وخضار",
                sizePrices = mapOf(ItemSize.MEDIUM to 30.0, ItemSize.LARGE to 40.0)
            )
        )
        add(
            MenuItem(
                id = "sy_sosis",
                nameAr = "ساندوتش سوسيس سوري",
                category = MenuCategory.SYRIAN,
                descriptionAr = "سوسيس متبل بالصوصات في خبز الصاج",
                sizePrices = mapOf(ItemSize.MEDIUM to 30.0, ItemSize.LARGE to 40.0)
            )
        )
        add(
            MenuItem(
                id = "sy_sausage",
                nameAr = "ساندوتش سجق سوري",
                category = MenuCategory.SYRIAN,
                descriptionAr = "سجق شرقي بخلطة الصاج السوري والجبن",
                sizePrices = mapOf(ItemSize.MEDIUM to 40.0, ItemSize.LARGE to 50.0)
            )
        )
        add(
            MenuItem(
                id = "sy_mix_meat",
                nameAr = "ساندوتش ميكس لحوم سوري",
                category = MenuCategory.SYRIAN,
                descriptionAr = "حجم كبير غني بميكس لحوم وسجق وبرجر وكفتة",
                sizePrices = mapOf(ItemSize.LARGE to 80.0)
            )
        )
        add(
            MenuItem(
                id = "sy_strips",
                nameAr = "ساندوتش ستربس سوري",
                category = MenuCategory.SYRIAN,
                descriptionAr = "قطع استربس مقرمشة مع تومية وصوص شيدر",
                sizePrices = mapOf(ItemSize.MEDIUM to 45.0, ItemSize.LARGE to 65.0)
            )
        )
        add(
            MenuItem(
                id = "sy_shawarma",
                nameAr = "ساندوتش شاورما سوري",
                category = MenuCategory.SYRIAN,
                descriptionAr = "شاورما بتتبيلة دمشقية خاصة مع تومية وخيار مخلل",
                sizePrices = mapOf(ItemSize.MEDIUM to 50.0, ItemSize.LARGE to 70.0)
            )
        )
        add(
            MenuItem(
                id = "sy_fajita",
                nameAr = "ساندوتش فاهيتا سوري",
                category = MenuCategory.SYRIAN,
                descriptionAr = "فاهيتا دجاج مع فلفل ألوان وبصل مشوح وصوصات",
                sizePrices = mapOf(ItemSize.MEDIUM to 50.0, ItemSize.LARGE to 70.0)
            )
        )
        add(
            MenuItem(
                id = "sy_mix_chicken",
                nameAr = "ساندوتش ميكس فراخ سوري",
                category = MenuCategory.SYRIAN,
                descriptionAr = "حجم كبير يجمع الشاورما والاستربس والبانيه",
                sizePrices = mapOf(ItemSize.LARGE to 80.0)
            )
        )
        add(
            MenuItem(
                id = "sy_risk_special",
                nameAr = "ساندوتش ريسك سوري الخاص",
                category = MenuCategory.SYRIAN,
                descriptionAr = "حجم كبير مميز جداً - تجميعة ريسك الكاملة في خبز الصاج المقرمش",
                sizePrices = mapOf(ItemSize.LARGE to 90.0),
                isRiskSpecial = true
            )
        )

        // --- 9. الكريب ---
        add(
            MenuItem(
                id = "cr_fries",
                nameAr = "كريب بطاطس",
                category = MenuCategory.CREPE,
                descriptionAr = "كريب مقرمش مثلث محشو بطاطس فارم فريتس، موتزاريلا وصوصات",
                priceSingle = 40.0
            )
        )
        add(
            MenuItem(
                id = "cr_burger",
                nameAr = "كريب برجر",
                category = MenuCategory.CREPE,
                descriptionAr = "قطع برجر لحم مشوي مع بطاطس وموتزاريلا وصوص رانش",
                priceSingle = 50.0
            )
        )
        add(
            MenuItem(
                id = "cr_kofta",
                nameAr = "كريب كفتة",
                category = MenuCategory.CREPE,
                descriptionAr = "كفتة بلدي مشوية مع صوصات وجبن وبطاطس",
                priceSingle = 50.0
            )
        )
        add(
            MenuItem(
                id = "cr_hotdog",
                nameAr = "كريب هوت دوج",
                category = MenuCategory.CREPE,
                descriptionAr = "هوت دوج مدخن مع ميكس أجبان وصوص باربكيو",
                priceSingle = 60.0
            )
        )
        add(
            MenuItem(
                id = "cr_sosis",
                nameAr = "كريب سوسيس",
                category = MenuCategory.CREPE,
                descriptionAr = "سوسيس مشوي مع موتزاريلا وبطاطس وصوصات",
                priceSingle = 60.0
            )
        )
        add(
            MenuItem(
                id = "cr_mix_meat",
                nameAr = "كريب مكس لحوم",
                category = MenuCategory.CREPE,
                descriptionAr = "برجر + كفتة + هوت دوج + سوسيس مع غرقان جبنة",
                priceSingle = 85.0
            )
        )
        add(
            MenuItem(
                id = "cr_mix_chicken",
                nameAr = "كريب مكس فراخ",
                category = MenuCategory.CREPE,
                descriptionAr = "استربس + زنجر + بانيه + فاهيتا مع صوصات ريسك",
                priceSingle = 95.0
            )
        )
        add(
            MenuItem(
                id = "cr_mix_cheese",
                nameAr = "كريب مكس جبن",
                category = MenuCategory.CREPE,
                descriptionAr = "موتزاريلا + رومي + شيدر + كيري لعشاق الجبن",
                priceSingle = 60.0
            )
        )
        add(
            MenuItem(
                id = "cr_strips",
                nameAr = "كريب استربس",
                category = MenuCategory.CREPE,
                descriptionAr = "دجاج استربس مقرمش ذهبي مع صوص تومية وشيدر",
                priceSingle = 70.0
            )
        )
        add(
            MenuItem(
                id = "cr_zinger",
                nameAr = "كريب زنجر",
                category = MenuCategory.CREPE,
                descriptionAr = "زنجر سبايسي حار ومقرمش مع هالبينو وجبن سايحة",
                priceSingle = 70.0
            )
        )
        add(
            MenuItem(
                id = "cr_pane",
                nameAr = "كريب بانيه",
                category = MenuCategory.CREPE,
                descriptionAr = "قطع بانيه مقرمشة مع بطاطس وموتزاريلا",
                priceSingle = 60.0
            )
        )
        add(
            MenuItem(
                id = "cr_chicken",
                nameAr = "كريب فراخ",
                category = MenuCategory.CREPE,
                descriptionAr = "صدور دجاج مشوية متبلة مع صوصات وجبن",
                priceSingle = 85.0
            )
        )
        add(
            MenuItem(
                id = "cr_fajita_chicken",
                nameAr = "كريب فاهيتا فراخ",
                category = MenuCategory.CREPE,
                descriptionAr = "فاهيتا فراخ مع فلفل ألوان وزيتون وصوصات مكسيكية",
                priceSingle = 90.0
            )
        )
        add(
            MenuItem(
                id = "cr_risk_special",
                nameAr = "كريب ريسك سوبر ديلوكس",
                category = MenuCategory.CREPE,
                descriptionAr = "ملك الكريبات! محشو لحوم وفراخ وإكسترا موتزاريلا وصوص ريسك السري",
                priceSingle = 120.0,
                isRiskSpecial = true
            )
        )

        // --- 10. المكرونات ---
        add(
            MenuItem(
                id = "pasta_bechamel",
                nameAr = "طاجن مكرونة بشاميل",
                category = MenuCategory.PASTA,
                descriptionAr = "مكرونة قلم مع لحم مفروم بلدي وصوص بشاميل غني وموتزاريلا محمرة",
                priceSingle = 70.0
            )
        )
        add(
            MenuItem(
                id = "pasta_negresco",
                nameAr = "طاجن مكرونة نجرسكو فراخ",
                category = MenuCategory.PASTA,
                descriptionAr = "مكرونة مع صدور دجاج متبلة، مشروم، صوص أبيض غني وطبقة جبنة سايحة",
                priceSingle = 90.0,
                isRiskSpecial = true
            )
        )
    }

    fun getItemsByCategory(category: MenuCategory): List<MenuItem> {
        if (category == MenuCategory.ALL) return menuItems
        return menuItems.filter { it.category == category }
    }

    fun searchItems(query: String): List<MenuItem> {
        if (query.isBlank()) return menuItems
        val q = query.trim().lowercase()
        return menuItems.filter {
            it.nameAr.contains(q, ignoreCase = true) ||
            it.descriptionAr.contains(q, ignoreCase = true) ||
            it.category.titleAr.contains(q, ignoreCase = true)
        }
    }

    fun getRiskSpecials(): List<MenuItem> {
        return menuItems.filter { it.isRiskSpecial }
    }
}
