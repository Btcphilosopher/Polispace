package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PolispaceApp(viewModel: PolispaceViewModel) {
    val isEnglish by viewModel.isEnglish.collectAsStateWithLifecycle()
    val role by viewModel.currentRole.collectAsStateWithLifecycle()
    val properties by viewModel.properties.collectAsStateWithLifecycle()
    val favorites by viewModel.favoriteIds.collectAsStateWithLifecycle()
    val selectedProperty by viewModel.selectedProperty.collectAsStateWithLifecycle()

    var showFilterSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column(
                modifier = Modifier
                    .background(SpaceDarkBg)
                    .statusBarsPadding()
            ) {
                // Main Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Logo & Slogan
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { viewModel.selectProperty(null) }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Brush.linearGradient(listOf(NeonCyan, CyberViolet)))
                                .drawBehind {
                                    // Subtle futuristic grid logo
                                    drawLine(
                                        color = Color.White.copy(alpha = 0.5f),
                                        start = Offset(0f, size.height / 2),
                                        end = Offset(size.width, size.height / 2),
                                        strokeWidth = 2f
                                    )
                                    drawLine(
                                        color = Color.White.copy(alpha = 0.5f),
                                        start = Offset(size.width / 2, 0f),
                                        end = Offset(size.width / 2, size.height),
                                        strokeWidth = 2f
                                    )
                                }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = if (isEnglish) "OPERATING SYSTEM" else "SYSTEM OPERACYJNY",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.8.sp,
                                    color = NeonCyan
                                )
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "POLI",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Light,
                                        letterSpacing = (-0.5).sp,
                                        color = Color.White
                                    )
                                )
                                Text(
                                    text = "SPACE",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = (-0.5).sp,
                                        color = Color.White
                                    )
                                )
                            }
                        }
                    }

                    // Bilingual Language Switcher
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(SpaceSurface)
                            .border(1.dp, SpaceBorderWhite, RoundedCornerShape(16.dp))
                            .clickable { viewModel.toggleLanguage() }
                            .padding(2.dp)
                            .testTag("lang_toggle_button"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (!isEnglish) NeonCyan else Color.Transparent)
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "PL",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (!isEnglish) Color.White else Color(0xFF64748B)
                                )
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isEnglish) NeonCyan else Color.Transparent)
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "EN",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isEnglish) Color.White else Color(0xFF64748B)
                                )
                            )
                        }
                    }
                }

                // Scrolling Role Tab Selector
                ScrollableTabRow(
                    selectedTabIndex = when (role) {
                        "BUYER" -> 0
                        "SELLER" -> 1
                        "AGENT_CRM" -> 2
                        else -> 3
                    },
                    containerColor = Color.Transparent,
                    contentColor = NeonCyan,
                    edgePadding = 12.dp,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[when (role) {
                                "BUYER" -> 0
                                "SELLER" -> 1
                                "AGENT_CRM" -> 2
                                else -> 3
                            }]),
                            color = NeonCyan
                        )
                    }
                ) {
                    Tab(
                        selected = role == "BUYER",
                        onClick = { viewModel.setRole("BUYER") },
                        modifier = Modifier.testTag("role_buyer_tab")
                    ) {
                        TabContent(
                            icon = Icons.Default.Search,
                            label = if (isEnglish) "Buy & Rent" else "Kup i Wynajmij",
                            selected = role == "BUYER"
                        )
                    }
                    Tab(
                        selected = role == "SELLER",
                        onClick = { viewModel.setRole("SELLER") },
                        modifier = Modifier.testTag("role_seller_tab")
                    ) {
                        TabContent(
                            icon = Icons.Default.AddHomeWork,
                            label = if (isEnglish) "Sell Property" else "Sprzedaj",
                            selected = role == "SELLER"
                        )
                    }
                    Tab(
                        selected = role == "AGENT_CRM",
                        onClick = { viewModel.setRole("AGENT_CRM") },
                        modifier = Modifier.testTag("role_crm_tab")
                    ) {
                        TabContent(
                            icon = Icons.Default.Business,
                            label = if (isEnglish) "Agent CRM" else "Panel Agenta",
                            selected = role == "AGENT_CRM"
                        )
                    }
                    Tab(
                        selected = role == "DEV_CONSOLE",
                        onClick = { viewModel.setRole("DEV_CONSOLE") },
                        modifier = Modifier.testTag("role_dev_tab")
                    ) {
                        TabContent(
                            icon = Icons.Default.Code,
                            label = if (isEnglish) "System Spec" else "Specyfikacja",
                            selected = role == "DEV_CONSOLE"
                        )
                    }
                }
            }
        },
        containerColor = SpaceDarkBg
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = role,
                transitionSpec = {
                    fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(220))
                },
                label = "role_navigation"
            ) { activeRole ->
                when (activeRole) {
                    "BUYER" -> {
                        if (selectedProperty != null) {
                            PropertyDetailScreen(viewModel = viewModel, isEnglish = isEnglish)
                        } else {
                            BuyerDashboard(
                                viewModel = viewModel,
                                isEnglish = isEnglish,
                                onFilterClick = { showFilterSheet = true }
                            )
                        }
                    }
                    "SELLER" -> {
                        SellerDashboard(viewModel = viewModel, isEnglish = isEnglish)
                    }
                    "AGENT_CRM" -> {
                        AgentCrmDashboard(viewModel = viewModel, isEnglish = isEnglish)
                    }
                    "DEV_CONSOLE" -> {
                        DeveloperConsoleScreen(isEnglish = isEnglish)
                    }
                }
            }

            // Simple Filter Sheet simulated overlay
            if (showFilterSheet) {
                FilterOverlay(
                    viewModel = viewModel,
                    isEnglish = isEnglish,
                    onDismiss = { showFilterSheet = false }
                )
            }
        }
    }
}

@Composable
fun TabContent(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, selected: Boolean) {
    Row(
        modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (selected) NeonCyan else Color(0xFF64748B),
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                color = if (selected) Color.White else Color(0xFF64748B)
            )
        )
    }
}

// ==========================================
// 1. BUYER DASHBOARD
// ==========================================
@Composable
fun BuyerDashboard(
    viewModel: PolispaceViewModel,
    isEnglish: Boolean,
    onFilterClick: () -> Unit
) {
    val properties by viewModel.properties.collectAsStateWithLifecycle()
    val favorites by viewModel.favoriteIds.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Card
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, SpaceBorder, RoundedCornerShape(16.dp))
            ) {
                // Read local custom hero
                Image(
                    painter = painterResource(id = R.drawable.img_hero_banner),
                    contentDescription = "Futuristic Warsaw Real Estate",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Dark glass scrim
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    SpaceDarkBg.copy(alpha = 0.85f)
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(CyberViolet.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
                            .border(0.5.dp, CyberViolet, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (isEnglish) "AI-POWERED OS" else "SYSTEM ZASILANY AI",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 9.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isEnglish) "Discover Future Living in Poland" else "Odkryj architekturę przyszłości",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            }
        }

        // Search and filter row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = { viewModel.searchQuery.value = it },
                    placeholder = {
                        Text(
                            text = if (isEnglish) "Search city, district, e.g., Mokotów..." else "Szukaj miasta, dzielnicy np. Mokotów...",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF94A3B8).copy(alpha = 0.6f))
                        )
                    },
                    leadingIcon = { Icon(Icons.Default.Search, "Search", tint = NeonCyan) },
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, SpaceBorderWhite, RoundedCornerShape(16.dp))
                        .testTag("search_input"),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = SpaceSurface,
                        unfocusedContainerColor = SpaceSurface,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = onFilterClick,
                    modifier = Modifier
                        .size(48.dp)
                        .background(SpaceSurface, RoundedCornerShape(16.dp))
                        .border(1.dp, SpaceBorderWhite, RoundedCornerShape(16.dp))
                        .testTag("filter_button")
                ) {
                    Icon(Icons.Default.FilterList, "Filter", tint = NeonCyan)
                }
            }
        }

        // Quick Stats / Price Trend Ribbon
        item {
            MarketInsightsRibbon(isEnglish = isEnglish)
        }

        // Active filters list
        item {
            ActiveFiltersBar(viewModel = viewModel, isEnglish = isEnglish)
        }

        // Property List Section
        item {
            Text(
                text = if (isEnglish) "Polispace Exclusive Listings (${properties.size})" else "Ekskluzywne Oferty Polispace (${properties.size})",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = NeonCyan
                )
            )
        }

        if (properties.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.SearchOff,
                            contentDescription = "No results",
                            tint = Color.White.copy(alpha = 0.3f),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isEnglish) "No properties match your filters." else "Brak ofert spełniających kryteria.",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.5f))
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        TextButton(onClick = { viewModel.resetFilters() }) {
                            Text(text = if (isEnglish) "Reset Filters" else "Wyczyść filtry", color = NeonCyan)
                        }
                    }
                }
            }
        } else {
            items(properties, key = { it.id }) { property ->
                val isFav = favorites.contains(property.id)
                PropertyCard(
                    property = property,
                    isFavorite = isFav,
                    isEnglish = isEnglish,
                    onToggleFavorite = { viewModel.toggleFavorite(property.id) },
                    onClick = { viewModel.selectProperty(property) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun MarketInsightsRibbon(isEnglish: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        InsightChip(
            title = if (isEnglish) "Warsaw Avg" else "Warszawa Śr.",
            value = "18,450 PLN/m²",
            trendUp = true,
            trendText = "+2.4%"
        )
        InsightChip(
            title = if (isEnglish) "Krakow Avg" else "Kraków Śr.",
            value = "15,800 PLN/m²",
            trendUp = true,
            trendText = "+1.8%"
        )
        InsightChip(
            title = if (isEnglish) "Gdansk Yield" else "Rentowność Gdańsk",
            value = "6.4% ROI",
            trendUp = false,
            trendText = "-0.2%"
        )
    }
}

@Composable
fun InsightChip(title: String, value: String, trendUp: Boolean, trendText: String) {
    Box(
        modifier = Modifier
            .background(SpaceSurface, RoundedCornerShape(16.dp))
            .border(1.dp, SpaceBorderWhite, RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column {
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color(0xFF64748B), // Slate 500
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Light,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = if (trendUp) Icons.Default.TrendingUp else Icons.Default.TrendingFlat,
                    contentDescription = "Trend",
                    tint = if (trendUp) NeonEmerald else NeonAmber,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = trendText,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = if (trendUp) NeonEmerald else NeonAmber,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Composable
fun ActiveFiltersBar(viewModel: PolispaceViewModel, isEnglish: Boolean) {
    val propType by viewModel.filterPropertyType.collectAsStateWithLifecycle()
    val marketType by viewModel.filterMarketType.collectAsStateWithLifecycle()
    val energy by viewModel.filterEnergyClass.collectAsStateWithLifecycle()
    val minPrice by viewModel.filterMinPrice.collectAsStateWithLifecycle()

    if (propType != null || marketType != null || energy != null || minPrice != null) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isEnglish) "Active:" else "Filtry:",
                style = MaterialTheme.typography.labelSmall.copy(color = NeonCyan)
            )

            propType?.let {
                FilterChipTag(text = it.name) { viewModel.filterPropertyType.value = null }
            }
            marketType?.let {
                FilterChipTag(text = if (it == MarketType.BUY) "KUPNO / BUY" else "NAJEM / RENT") { viewModel.filterMarketType.value = null }
            }
            energy?.let {
                FilterChipTag(text = "Energy: ${it.name}") { viewModel.filterEnergyClass.value = null }
            }
            minPrice?.let {
                FilterChipTag(text = "Min Price") { viewModel.filterMinPrice.value = null }
            }

            TextButton(onClick = { viewModel.resetFilters() }) {
                Text(text = if (isEnglish) "Clear" else "Wyczyść", fontSize = 11.sp, color = CyberViolet)
            }
        }
    }
}

@Composable
fun FilterChipTag(text: String, onRemove: () -> Unit) {
    Box(
        modifier = Modifier
            .background(NeonCyan.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
            .border(1.dp, NeonCyan.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .clickable { onRemove() }
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = text, style = MaterialTheme.typography.labelSmall.copy(color = Color.White))
            Spacer(modifier = Modifier.width(4.dp))
            Icon(Icons.Default.Close, "Remove", tint = Color.White.copy(0.6f), modifier = Modifier.size(10.dp))
        }
    }
}

@Composable
fun PropertyCard(
    property: Property,
    isFavorite: Boolean,
    isEnglish: Boolean,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SpaceSurface)
            .border(1.dp, SpaceBorderWhite, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(12.dp)
            .testTag("property_card_${property.id}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Visual Block (Simulating futuristic isometric layout or custom color scheme)
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                SpaceSurfaceVariant,
                                SpaceDarkBg
                            )
                        )
                    )
                    .drawBehind {
                        // Procedural graphic
                        drawCircle(
                            color = if (property.marketType == MarketType.BUY) NeonCyan.copy(alpha = 0.15f) else CyberViolet.copy(alpha = 0.15f),
                            radius = size.minDimension / 2.5f,
                            center = Offset(size.width / 2, size.height / 2)
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = when (property.propertyType) {
                            PropertyType.APARTMENT -> Icons.Default.Apartment
                            PropertyType.HOUSE -> Icons.Default.Home
                            PropertyType.STUDIO -> Icons.Default.Hotel
                            PropertyType.COMMERCIAL -> Icons.Default.Storefront
                        },
                        contentDescription = "Type",
                        tint = if (property.marketType == MarketType.BUY) NeonCyan else CyberViolet,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${property.areaSqm.toInt()} m²",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            // Text Details
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Energy Efficiency Badge
                    Box(
                        modifier = Modifier
                            .background(
                                color = when (property.energyClass) {
                                    EnergyClass.A -> NeonEmerald.copy(alpha = 0.2f)
                                    EnergyClass.B, EnergyClass.C -> NeonAmber.copy(alpha = 0.2f)
                                    else -> Color.Red.copy(alpha = 0.2f)
                                },
                                shape = RoundedCornerShape(4.dp)
                            )
                            .border(
                                width = 0.5.dp,
                                color = when (property.energyClass) {
                                    EnergyClass.A -> NeonEmerald
                                    EnergyClass.B, EnergyClass.C -> NeonAmber
                                    else -> Color.Red
                                },
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "ENERGY: ${property.energyClass.name}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 8.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Rent vs Buy Badge
                    Box(
                        modifier = Modifier
                            .background(
                                if (property.marketType == MarketType.BUY) NeonCyan.copy(alpha = 0.1f) else CyberViolet.copy(alpha = 0.1f),
                                RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (property.marketType == MarketType.BUY) {
                                if (isEnglish) "BUY" else "KUPNO"
                            } else {
                                if (isEnglish) "RENT" else "NAJEM"
                            },
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 8.sp,
                                color = if (property.marketType == MarketType.BUY) NeonCyan else CyberViolet,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = property.title.get(isEnglish),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color.White),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "${property.city}, ${property.district.get(isEnglish)}",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.White.copy(alpha = 0.5f))
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (property.marketType == MarketType.BUY) {
                            String.format("%,.0f PLN", property.price)
                        } else {
                            String.format("%,.0f PLN / msc", property.price)
                        },
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = NeonCyan
                        )
                    )

                    // Property Specs overview
                    Text(
                        text = "${property.rooms} ${if (isEnglish) "rooms" else "pok."} | ${if (isEnglish) "Yr" else "Rok"} ${property.buildYear}",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(0.4f))
                    )
                }
            }

            // Favorite Button
            IconButton(
                onClick = onToggleFavorite,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) Color.Red else Color.White.copy(alpha = 0.4f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// ==========================================
// 2. FILTER OVERLAY
// ==========================================
@Composable
fun FilterOverlay(
    viewModel: PolispaceViewModel,
    isEnglish: Boolean,
    onDismiss: () -> Unit
) {
    val propType by viewModel.filterPropertyType.collectAsStateWithLifecycle()
    val marketType by viewModel.filterMarketType.collectAsStateWithLifecycle()
    val energy by viewModel.filterEnergyClass.collectAsStateWithLifecycle()
    val minPrice by viewModel.filterMinPrice.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.75f))
            .clickable { onDismiss() }
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(SpaceSurface, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .border(1.dp, SpaceBorder, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .clickable(enabled = false) {} // Prevent click-through
                .padding(24.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Drag handle
                Box(
                    modifier = Modifier
                        .size(40.dp, 4.dp)
                        .background(Color.White.copy(0.3f), RoundedCornerShape(2.dp))
                        .align(Alignment.CenterHorizontally)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isEnglish) "Advanced Intelligence Filters" else "Zaawansowane filtry wyszukiwania",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = NeonCyan)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, "Close", tint = Color.White)
                    }
                }

                // Property Type Selection
                Column {
                    Text(
                        text = if (isEnglish) "PROPERTY TYPE" else "TYP NIERUCHOMOŚCI",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(0.6f), fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PropertyType.values().forEach { type ->
                            val selected = propType == type
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        if (selected) NeonCyan.copy(alpha = 0.2f) else SpaceSurfaceVariant,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .border(
                                        1.dp,
                                        if (selected) NeonCyan else Color.Transparent,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable {
                                        viewModel.filterPropertyType.value = if (selected) null else type
                                    }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = type.name,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (selected) NeonCyan else Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                }

                // Market Type
                Column {
                    Text(
                        text = if (isEnglish) "MARKET CONTRACT" else "RODZAJ UMOWY",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(0.6f), fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MarketType.values().forEach { mType ->
                            val selected = marketType == mType
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        if (selected) CyberViolet.copy(alpha = 0.2f) else SpaceSurfaceVariant,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .border(
                                        1.dp,
                                        if (selected) CyberViolet else Color.Transparent,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable {
                                        viewModel.filterMarketType.value = if (selected) null else mType
                                    }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (mType == MarketType.BUY) {
                                        if (isEnglish) "PURCHASE" else "KUPNO (Księga Wieczysta)"
                                    } else {
                                        if (isEnglish) "RENTAL" else "NAJEM"
                                    },
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (selected) CyberViolet else Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                }

                // Energy Efficiency Class
                Column {
                    Text(
                        text = if (isEnglish) "MINIMUM ENERGY RATING (EU STANDARDS)" else "MINIMALNA KLASA ENERGETYCZNA (NORMY UE)",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(0.6f), fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        EnergyClass.values().take(4).forEach { cls ->
                            val selected = energy == cls
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        if (selected) NeonEmerald.copy(alpha = 0.2f) else SpaceSurfaceVariant,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .border(
                                        1.dp,
                                        if (selected) NeonEmerald else Color.Transparent,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable {
                                        viewModel.filterEnergyClass.value = if (selected) null else cls
                                    }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = cls.name,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (selected) NeonEmerald else Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                }

                // Minimum price selector simulation
                Column {
                    Text(
                        text = if (isEnglish) "MINIMUM PRICE OFFSET" else "MINIMALNY PRÓG CENOWY",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(0.6f), fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(50000.0, 500000.0, 1500000.0).forEach { price ->
                            val selected = minPrice == price
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        if (selected) NeonCyan.copy(alpha = 0.15f) else SpaceSurfaceVariant,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .border(0.5.dp, if (selected) NeonCyan else Color.White.copy(0.1f), RoundedCornerShape(8.dp))
                                    .clickable { viewModel.filterMinPrice.value = if (selected) null else price }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = String.format(">= %,.0f PLN", price),
                                    style = MaterialTheme.typography.labelSmall.copy(color = Color.White, fontSize = 9.sp)
                                )
                            }
                        }
                    }
                }

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = SpaceDarkBg),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("apply_filters_button")
                ) {
                    Text(
                        text = if (isEnglish) "Apply Intelligence Filters" else "Zastosuj Inteligentne Filtry",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

// ==========================================
// 3. PROPERTY DETAIL SHEET
// ==========================================
@Composable
fun PropertyDetailScreen(viewModel: PolispaceViewModel, isEnglish: Boolean) {
    val property by viewModel.selectedProperty.collectAsStateWithLifecycle()
    val view3dMode by viewModel.view3dMode.collectAsStateWithLifecycle()
    val arSimulationActive by viewModel.arSimulationActive.collectAsStateWithLifecycle()
    val bids by viewModel.activeBids.collectAsStateWithLifecycle()
    val aiValuation by viewModel.aiValuationResult.collectAsStateWithLifecycle()
    val aiLoading by viewModel.isAiValuationLoading.collectAsStateWithLifecycle()

    val prop = property ?: return

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SpaceDarkBg)
    ) {
        // Hero Walkthrough Box
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                if (view3dMode) {
                    // Panoramic 360 Walkthrough Mockup with actual horizontal gesture drag!
                    Simulated360Walkthrough(isEnglish = isEnglish)
                } else {
                    // Regular Static Image
                    Image(
                        painter = painterResource(id = R.drawable.img_hero_banner),
                        contentDescription = prop.title.get(isEnglish),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Top Floating Toolbar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.selectProperty(null) },
                        modifier = Modifier
                            .background(Color.Black.copy(0.6f), RoundedCornerShape(12.dp))
                            .testTag("back_to_list_button")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { viewModel.view3dMode.value = !view3dMode },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (view3dMode) NeonCyan else Color.Black.copy(0.6f),
                                contentColor = if (view3dMode) SpaceDarkBg else Color.White
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.ViewInAr, null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (view3dMode) "360° ON" else "360° TOUR",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }

                // Subtitle Overlay banner
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color.Black.copy(0.9f))
                            )
                        )
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text(
                                text = prop.title.get(isEnglish),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = prop.address.get(isEnglish),
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.White.copy(0.6f))
                            )
                        }

                        // Energy Certificate Icon
                        Box(
                            modifier = Modifier
                                .background(NeonEmerald, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "EU Energy: ${prop.energyClass.name}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = SpaceDarkBg,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
        }

        // Action Toolbar
        item {
            Column(modifier = Modifier.fillMaxWidth().background(SpaceSurface)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    QuickActionIcon(icon = Icons.Default.Key, text = if (isEnglish) "Book Tour" else "Rezerwacja")
                    QuickActionIcon(icon = Icons.Default.ContactPhone, text = if (isEnglish) "Contact" else "Kontakt")
                    QuickActionIcon(icon = Icons.Default.Share, text = if (isEnglish) "Share" else "Udostępnij")
                }
                HorizontalDivider(color = SpaceBorderWhite)
            }
        }

        // Details Panel
        item {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {

                // Core attributes grid
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SpaceSurface, RoundedCornerShape(12.dp))
                        .border(0.5.dp, SpaceBorderWhite, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            PropertySpecChip(title = if (isEnglish) "PRICE / CENA" else "CENA", value = String.format("%,.0f PLN", prop.price), highlight = true)
                            PropertySpecChip(title = if (isEnglish) "AREA / METRAŻ" else "METRAŻ", value = "${prop.areaSqm} m²")
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            PropertySpecChip(title = if (isEnglish) "ROOMS / POKOJE" else "POKOJE", value = "${prop.rooms}")
                            PropertySpecChip(title = if (isEnglish) "BUILT / ROK" else "ROK BUDOWY", value = "${prop.buildYear}")
                        }
                    }
                }

                // AI Valuation Report Block
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    CyberViolet.copy(alpha = 0.08f),
                                    NeonCyan.copy(alpha = 0.05f)
                                )
                            ),
                            RoundedCornerShape(16.dp)
                        )
                        .border(1.dp, SpaceBorder, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AutoAwesome, "AI", tint = NeonCyan, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "POLISPACE AI REPORT",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp,
                                        color = NeonCyan
                                    )
                                )
                            }

                            // Dynamic reload
                            IconButton(onClick = { viewModel.triggerAiValuation(prop) }) {
                                Icon(Icons.Default.Refresh, "Refresh AI", tint = NeonCyan, modifier = Modifier.size(16.dp))
                            }
                        }

                        if (aiLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    CircularProgressIndicator(color = NeonCyan, modifier = Modifier.size(24.dp))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = if (isEnglish) "Analyzing spatial district intelligence..." else "Analizowanie bazy danych przestrzennych...",
                                        fontSize = 11.sp,
                                        color = Color.White.copy(0.6f)
                                    )
                                }
                            }
                        } else {
                            Text(
                                text = aiValuation,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.White.copy(alpha = 0.85f),
                                    lineHeight = 18.sp
                                )
                            )
                        }
                    }
                }

                // Description
                Column {
                    Text(
                        text = if (isEnglish) "About Property" else "O nieruchomości",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = NeonCyan)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = prop.description.get(isEnglish),
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.White.copy(0.7f), lineHeight = 18.sp)
                    )
                }

                // Map Intelligence Overlay Panel
                MapIntelligenceOverlay(prop = prop, isEnglish = isEnglish)

                // Simulated Land Registry Section
                PolishLandRegistryInspection(viewModel = viewModel, prop = prop, isEnglish = isEnglish)

                // Interactive Mortgage Repayment Estimator
                InteractiveMortgagePanel(viewModel = viewModel, isEnglish = isEnglish)

                // Real-time Offers Engine (WebSocket simulator)
                RealTimeBiddingPanel(viewModel = viewModel, propId = prop.id, isEnglish = isEnglish, propPrice = prop.price)
            }
        }
    }
}

@Composable
fun Simulated360Walkthrough(isEnglish: Boolean) {
    var dragOffset by remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    dragOffset += dragAmount.x
                }
            },
        contentAlignment = Alignment.Center
    ) {
        // Draw a simulated interactive wireframe panoramic grid that reacts to finger swiping!
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stepX = 40f
            val stepY = 40f
            val shift = dragOffset % size.width

            for (x in -20..20) {
                val drawX = x * stepX + shift + (size.width / 2)
                drawLine(
                    color = NeonCyan.copy(alpha = 0.15f),
                    start = Offset(drawX, 0f),
                    end = Offset(drawX, size.height),
                    strokeWidth = 1f
                )
            }
            for (y in 0..10) {
                val drawY = y * stepY + (size.height / 4)
                drawLine(
                    color = CyberViolet.copy(alpha = 0.15f),
                    start = Offset(0f, drawY),
                    end = Offset(size.width, drawY),
                    strokeWidth = 1f
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(Color.Black.copy(0.5f), RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Icon(Icons.Default.Swipe, "Drag to view", tint = NeonCyan, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (isEnglish) "Swipe horizontally to rotate 360°" else "Przeciągnij w poziomie, aby obrócić 360°",
                style = MaterialTheme.typography.labelSmall.copy(color = Color.White, fontSize = 9.sp)
            )
            Text(
                text = if (isEnglish) "Virtual Tour Module Active" else "Moduł Wirtualnego Spaceru Aktywny",
                style = MaterialTheme.typography.labelSmall.copy(color = NeonCyan, fontSize = 8.sp, fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
fun QuickActionIcon(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable {  }
    ) {
        Icon(imageVector = icon, contentDescription = text, tint = NeonCyan, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = text, style = MaterialTheme.typography.labelSmall.copy(color = Color.White))
    }
}

@Composable
fun PropertySpecChip(title: String, value: String, highlight: Boolean = false) {
    Column(modifier = Modifier.padding(4.dp)) {
        Text(text = title, style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(0.4f)))
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = if (highlight) NeonCyan else Color.White
            )
        )
    }
}

@Composable
fun MapIntelligenceOverlay(prop: Property, isEnglish: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(SpaceSurface, RoundedCornerShape(12.dp))
            .border(0.5.dp, SpaceBorder, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = if (isEnglish) "POLAND URBAN GEOSPATIAL DATA" else "DANE GEOPRZESTRZENNE - METRYKI MIEJSKIE",
                style = MaterialTheme.typography.labelSmall.copy(color = NeonCyan, fontWeight = FontWeight.Bold)
            )

            // Crime index
            MetricRatingRow(
                label = if (isEnglish) "Safety Index / Wskaźnik Bezpieczeństwa" else "Wskaźnik Bezpieczeństwa",
                value = "${10.0 - prop.crimeRateScore}/10",
                progress = ((10.0 - prop.crimeRateScore) / 10f).toFloat(),
                color = NeonEmerald
            )

            // Transport access
            MetricRatingRow(
                label = if (isEnglish) "Transit & Bus Proximity / Komunikacja" else "Komunikacja (Metro/Tramwaj)",
                value = "${prop.transportAccessScore}/10",
                progress = (prop.transportAccessScore / 10f).toFloat(),
                color = ElectricBlue
            )

            // Schools and infrastructure
            MetricRatingRow(
                label = if (isEnglish) "Education rating / Edukacja i Szkoły" else "Edukacja i Szkoły",
                value = "${prop.schoolRatingScore}/10",
                progress = (prop.schoolRatingScore / 10f).toFloat(),
                color = CyberViolet
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Land classification zone details
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SpaceDarkBg, RoundedCornerShape(8.dp))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Explore, "GIS", tint = NeonCyan, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = if (isEnglish) "Polish Land Class Plan (Plan Zagospodarowania)" else "Miejscowy Plan Zagospodarowania (MPZP)",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(0.5f), fontSize = 8.sp)
                    )
                    Text(
                        text = prop.infrastructureZone.get(isEnglish),
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.White, fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
fun MetricRatingRow(label: String, value: String, progress: Float, color: Color) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = label, style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(0.7f)))
            Text(text = value, style = MaterialTheme.typography.labelSmall.copy(color = color, fontWeight = FontWeight.Bold))
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp)),
            color = color,
            trackColor = SpaceDarkBg
        )
    }
}

@Composable
fun PolishLandRegistryInspection(viewModel: PolispaceViewModel, prop: Property, isEnglish: Boolean) {
    val info = viewModel.getLandRegistry(prop.id)
    var isExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(SpaceSurface, RoundedCornerShape(12.dp))
            .border(0.5.dp, SpaceBorderWhite, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Dns, "Registry", tint = CyberViolet, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isEnglish) "Land Registry Info (Księgi Wieczyste)" else "Wgląd do Księgi Wieczystej (KW)",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = Color.White)
                    )
                }
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = "Expand",
                    tint = Color.White
                )
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = SpaceBorderWhite)
                Spacer(modifier = Modifier.height(8.dp))

                RegistryDataRow(label = if (isEnglish) "KW Identification No." else "Numer Księgi Wieczystej", value = info.kwNumber)
                RegistryDataRow(label = if (isEnglish) "Registered Owner" else "Właściciel w Dziale II", value = info.owner)
                RegistryDataRow(label = if (isEnglish) "Mortgage Claims (Section III)" else "Hipoteki (Dział III)", value = info.mortgagesSection3, isAlert = info.mortgagesSection3.contains("Hipoteka"))
                RegistryDataRow(label = if (isEnglish) "Rights & Servitudes (Section IV)" else "Prawa i Roszczenia (Dział IV)", value = info.rightsSection4)

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(NeonEmerald.copy(0.1f), RoundedCornerShape(6.dp))
                        .padding(6.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Verified, "Verified", tint = NeonEmerald, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isEnglish) "Verified with Central Land Registry (MS.GOV.PL)" else "Zweryfikowano z Centralną Bazą MS.GOV.PL",
                        style = MaterialTheme.typography.labelSmall.copy(color = NeonEmerald, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
fun RegistryDataRow(label: String, value: String, isAlert: Boolean = false) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(0.4f), fontSize = 8.sp))
        Text(
            text = value,
            style = MaterialTheme.typography.labelSmall.copy(
                color = if (isAlert) NeonAmber else Color.White,
                fontWeight = FontWeight.Medium
            )
        )
    }
}

@Composable
fun InteractiveMortgagePanel(viewModel: PolispaceViewModel, isEnglish: Boolean) {
    val price by viewModel.mortgagePrice.collectAsStateWithLifecycle()
    val deposit by viewModel.mortgageDeposit.collectAsStateWithLifecycle()
    val years by viewModel.mortgageYears.collectAsStateWithLifecycle()
    val rate by viewModel.mortgageRate.collectAsStateWithLifecycle()
    val result by viewModel.mortgageResult.collectAsStateWithLifecycle()

    var isExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(SpaceSurface, RoundedCornerShape(12.dp))
            .border(0.5.dp, SpaceBorderWhite, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Calculate, "Mortgage", tint = NeonCyan, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isEnglish) "Polish Mortgage Calculator" else "Kalkulator Kredytu Hipotecznego",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = Color.White)
                    )
                }
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = "Expand",
                    tint = Color.White
                )
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = SpaceBorderWhite)
                Spacer(modifier = Modifier.height(12.dp))

                // deposit slider
                Text(
                    text = "${if (isEnglish) "Downpayment" else "Wkład Własny"}: ${String.format("%,.0f PLN", deposit)} (${(deposit / price * 100).toInt()}%)",
                    style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(0.8f))
                )
                Slider(
                    value = deposit.toFloat(),
                    onValueChange = { viewModel.mortgageDeposit.value = it.toDouble() },
                    valueRange = 0f..price.toFloat(),
                    colors = SliderDefaults.colors(thumbColor = NeonCyan, activeTrackColor = NeonCyan)
                )

                // loan term slider
                Text(
                    text = "${if (isEnglish) "Loan Term" else "Okres Spłaty"}: $years ${if (isEnglish) "years" else "lat"}",
                    style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(0.8f))
                )
                Slider(
                    value = years.toFloat(),
                    onValueChange = { viewModel.mortgageYears.value = it.toInt() },
                    valueRange = 5f..35f,
                    colors = SliderDefaults.colors(thumbColor = NeonCyan, activeTrackColor = NeonCyan)
                )

                // interest rate slider
                Text(
                    text = "${if (isEnglish) "Interest Rate" else "Oprocentowanie"}: $rate % (WIBOR 3M / 6M)",
                    style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(0.8f))
                )
                Slider(
                    value = rate.toFloat(),
                    onValueChange = { viewModel.mortgageRate.value = it.toDouble() },
                    valueRange = 1.0f..12.0f,
                    colors = SliderDefaults.colors(thumbColor = NeonCyan, activeTrackColor = NeonCyan)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Results Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SpaceDarkBg, RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = if (isEnglish) "Est. Monthly Payment" else "Szacowana Rata", style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(0.6f)))
                            Text(
                                text = String.format("%,.2f PLN / msc", result.monthlyPayment),
                                style = MaterialTheme.typography.bodyMedium.copy(color = NeonCyan, fontWeight = FontWeight.Bold)
                            )
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = if (isEnglish) "Credit Eligibility Score" else "Zdolność Kredytowa", style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(0.6f)))
                            Text(
                                text = result.riskRating,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = if (result.isApproved) NeonEmerald else NeonAmber,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        if (!result.isApproved) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isEnglish) "Note: Polish banks typically require minimum 10% downpayment." else "Uwaga: KNF wymaga minimum 10-20% wkładu własnego.",
                                style = MaterialTheme.typography.labelSmall.copy(color = NeonAmber, fontSize = 8.sp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RealTimeBiddingPanel(viewModel: PolispaceViewModel, propId: String, isEnglish: Boolean, propPrice: Double) {
    val bidsMap by viewModel.activeBids.collectAsStateWithLifecycle()
    val activeBidsList = bidsMap[propId] ?: emptyList()
    var userOfferAmount by remember { mutableStateOf("") }
    var notificationMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(SpaceSurface, RoundedCornerShape(12.dp))
            .border(0.5.dp, SpaceBorder, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Waves, "WebSockets", tint = NeonCyan, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isEnglish) "LIVE OFFER STREAM (WEBSOCKETS)" else "STRUMIEŃ OFERT LIVE (WEBSOCKETS)",
                    style = MaterialTheme.typography.labelSmall.copy(color = NeonCyan, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                )
            }

            // List of active bids
            if (activeBidsList.isEmpty()) {
                Text(
                    text = if (isEnglish) "No active digital offers on this property. Be the first!" else "Brak aktywnych cyfrowych ofert na tę nieruchomość.",
                    style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(0.5f))
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    activeBidsList.take(3).forEach { bid ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(SpaceDarkBg, RoundedCornerShape(6.dp))
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AccountCircle, null, tint = Color.White.copy(0.4f), modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = bid.bidder, style = MaterialTheme.typography.labelSmall.copy(color = Color.White))
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = String.format("%,.0f PLN", bid.amount),
                                    style = MaterialTheme.typography.labelSmall.copy(color = NeonCyan, fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = bid.timeAgo, style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(0.4f), fontSize = 8.sp))
                            }
                        }
                    }
                }
            }

            // Place dynamic bid input
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = userOfferAmount,
                    onValueChange = { userOfferAmount = it },
                    placeholder = {
                        Text(
                            text = if (isEnglish) "Offer in PLN..." else "Kwota oferty PLN...",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(0.4f))
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .testTag("bidding_amount_input"),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = SpaceDarkBg,
                        unfocusedContainerColor = SpaceDarkBg,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        val amt = userOfferAmount.toDoubleOrNull()
                        if (amt != null && amt > 0) {
                            viewModel.submitNewOffer(propId, "You (User_PL)", amt)
                            userOfferAmount = ""
                            notificationMessage = if (isEnglish) "Offer Broadcast Successful!" else "Pomyślnie wysłano ofertę kupna!"
                        } else {
                            notificationMessage = if (isEnglish) "Invalid Amount" else "Nieprawidłowa kwota"
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = SpaceDarkBg),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .height(48.dp)
                        .testTag("submit_bid_button")
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, null, modifier = Modifier.size(16.dp))
                }
            }

            if (notificationMessage.isNotEmpty()) {
                Text(
                    text = notificationMessage,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = if (notificationMessage.contains("Success") || notificationMessage.contains("Pomyślnie")) NeonEmerald else NeonAmber,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}


// ==========================================
// 4. SELLER MODULE & AI VALUATION RECOMMENDATION
// ==========================================
@Composable
fun SellerDashboard(viewModel: PolispaceViewModel, isEnglish: Boolean) {
    val titlePl by viewModel.sellerTitlePl.collectAsStateWithLifecycle()
    val titleEn by viewModel.sellerTitleEn.collectAsStateWithLifecycle()
    val descPl by viewModel.sellerDescPl.collectAsStateWithLifecycle()
    val descEn by viewModel.sellerDescEn.collectAsStateWithLifecycle()
    val addressPl by viewModel.sellerAddressPl.collectAsStateWithLifecycle()
    val addressEn by viewModel.sellerAddressEn.collectAsStateWithLifecycle()
    val city by viewModel.sellerCity.collectAsStateWithLifecycle()
    val districtPl by viewModel.sellerDistrictPl.collectAsStateWithLifecycle()
    val districtEn by viewModel.sellerDistrictEn.collectAsStateWithLifecycle()
    val price by viewModel.sellerPrice.collectAsStateWithLifecycle()
    val area by viewModel.sellerArea.collectAsStateWithLifecycle()
    val rooms by viewModel.sellerRooms.collectAsStateWithLifecycle()
    val floor by viewModel.sellerFloor.collectAsStateWithLifecycle()
    val buildYear by viewModel.sellerBuildYear.collectAsStateWithLifecycle()
    val pType by viewModel.sellerType.collectAsStateWithLifecycle()
    val mType by viewModel.sellerMarketType.collectAsStateWithLifecycle()
    val energyCls by viewModel.sellerEnergyClass.collectAsStateWithLifecycle()

    val aiResult by viewModel.aiValuationResult.collectAsStateWithLifecycle()
    val aiLoading by viewModel.isAiValuationLoading.collectAsStateWithLifecycle()

    var showSuccessMessage by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = if (isEnglish) "Publish Property to Poland's Market" else "Wystaw nieruchomość na rynek Polski",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = NeonCyan)
            )
            Text(
                text = if (isEnglish) "Complete the spatial metrics of your property below." else "Wypełnij parametry przestrzenne nieruchomości poniżej.",
                style = MaterialTheme.typography.bodySmall.copy(color = Color.White.copy(0.5f))
            )
        }

        // AI pricing engine trigger block
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            listOf(
                                SpaceSurface,
                                CyberViolet.copy(alpha = 0.15f)
                            )
                        ),
                        RoundedCornerShape(16.dp)
                    )
                    .border(1.dp, SpaceBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, "AI", tint = NeonCyan)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isEnglish) "POLISPACE AI VALUE ESTIMATOR" else "SZACOWANIE CENY POLISPACE AI",
                            style = MaterialTheme.typography.labelSmall.copy(color = NeonCyan, fontWeight = FontWeight.Bold)
                        )
                    }
                    Text(
                        text = if (isEnglish) "Our real-estate neural model queries municipal datasets (m², district, year) to suggest optimal Pricing." else "Nasz silnik analizuje dane miejskie (m², dzielnica, wiek) w celu oszacowania ceny ofertowej.",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(0.7f))
                    )
                    Button(
                        onClick = { viewModel.triggerCustomAiValuation() },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberViolet, contentColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (isEnglish) "Request AI Pricing Advice" else "Generuj rekomendację cenową AI",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    if (aiLoading) {
                        CircularProgressIndicator(color = NeonCyan, modifier = Modifier.size(20.dp))
                    } else if (aiResult.isNotEmpty()) {
                        HorizontalDivider(color = SpaceBorderWhite)
                        Text(
                            text = aiResult,
                            style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(0.9f))
                        )
                    }
                }
            }
        }

        // Basic inputs
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SellerTextField(
                    label = if (isEnglish) "Title (Polish)" else "Tytuł (Polski)",
                    value = titlePl,
                    onValueChange = { viewModel.sellerTitlePl.value = it },
                    tag = "seller_title_pl"
                )
                SellerTextField(
                    label = if (isEnglish) "Title (English)" else "Tytuł (Angielski)",
                    value = titleEn,
                    onValueChange = { viewModel.sellerTitleEn.value = it },
                    tag = "seller_title_en"
                )
                SellerTextField(
                    label = if (isEnglish) "Address / Adres" else "Adres",
                    value = addressPl,
                    onValueChange = { viewModel.sellerAddressPl.value = it },
                    tag = "seller_address"
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(modifier = Modifier.weight(1f)) {
                        SellerTextField(
                            label = if (isEnglish) "City / Miasto" else "Miasto",
                            value = city,
                            onValueChange = { viewModel.sellerCity.value = it },
                            tag = "seller_city"
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        SellerTextField(
                            label = if (isEnglish) "District / Dzielnica" else "Dzielnica",
                            value = districtPl,
                            onValueChange = { viewModel.sellerDistrictPl.value = it },
                            tag = "seller_district"
                        )
                    }
                }
            }
        }

        // Size and Metrics
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    SellerTextField(
                        label = if (isEnglish) "Area (m²)" else "Metraż (m²)",
                        value = area,
                        onValueChange = { viewModel.sellerArea.value = it },
                        tag = "seller_area"
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    SellerTextField(
                        label = if (isEnglish) "Price (PLN)" else "Cena (PLN)",
                        value = price,
                        onValueChange = { viewModel.sellerPrice.value = it },
                        tag = "seller_price"
                    )
                }
            }
        }

        // Rooms and structure
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    SellerTextField(label = if (isEnglish) "Rooms" else "Pokoje", value = rooms, onValueChange = { viewModel.sellerRooms.value = it }, tag = "seller_rooms")
                }
                Box(modifier = Modifier.weight(1f)) {
                    SellerTextField(label = if (isEnglish) "Floor" else "Piętro", value = floor, onValueChange = { viewModel.sellerFloor.value = it }, tag = "seller_floor")
                }
                Box(modifier = Modifier.weight(1f)) {
                    SellerTextField(label = if (isEnglish) "Build Year" else "Rok budowy", value = buildYear, onValueChange = { viewModel.sellerBuildYear.value = it }, tag = "seller_year")
                }
            }
        }

        // Dropdown type togglers simulation
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = if (isEnglish) "PROPERTY CLASSIFICATION" else "KLASYFIKACJA NIERUCHOMOŚCI",
                    style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(0.5f))
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    PropertyType.values().forEach { t ->
                        val active = pType == t
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(if (active) NeonCyan.copy(0.2f) else SpaceSurface, RoundedCornerShape(8.dp))
                                .border(0.5.dp, if (active) NeonCyan else SpaceBorderWhite, RoundedCornerShape(8.dp))
                                .clickable { viewModel.sellerType.value = t }
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = t.name, style = MaterialTheme.typography.labelSmall.copy(color = if (active) NeonCyan else Color.White))
                        }
                    }
                }
            }
        }

        // Contract type
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MarketType.values().forEach { m ->
                    val active = mType == m
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(if (active) CyberViolet.copy(0.2f) else SpaceSurface, RoundedCornerShape(8.dp))
                            .border(0.5.dp, if (active) CyberViolet else SpaceBorderWhite, RoundedCornerShape(8.dp))
                            .clickable { viewModel.sellerMarketType.value = m }
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (m == MarketType.BUY) {
                                if (isEnglish) "FOR SALE" else "NA SPRZEDAŻ"
                            } else {
                                if (isEnglish) "FOR RENT" else "NA WYNAJEM"
                            },
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (active) CyberViolet else Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }

        // Submission trigger
        item {
            Button(
                onClick = {
                    viewModel.submitSellerListing {
                        showSuccessMessage = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = SpaceDarkBg),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("publish_listing_button")
            ) {
                Text(
                    text = if (isEnglish) "Publish Listing to POLISPACE" else "Opublikuj Ofertę w POLISPACE",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }

        if (showSuccessMessage) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(NeonEmerald.copy(0.15f), RoundedCornerShape(8.dp))
                        .border(1.dp, NeonEmerald, RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Text(
                            text = if (isEnglish) "Success!" else "Sukces!",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = NeonEmerald)
                        )
                        Text(
                            text = if (isEnglish) "Your property is now listed in the main marketplace." else "Twoje ogłoszenie zostało pomyślnie zapisane lokalnie i jest widoczne w wyszukiwarce głównej.",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color.White)
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SellerTextField(label: String, value: String, onValueChange: (String) -> Unit, tag: String) {
    Column {
        Text(text = label, style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(0.6f)))
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .testTag(tag),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = SpaceSurface,
                unfocusedContainerColor = SpaceSurface,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true
        )
    }
}


// ==========================================
// 5. AGENT & AGENCY CRM
// ==========================================
@Composable
fun AgentCrmDashboard(viewModel: PolispaceViewModel, isEnglish: Boolean) {
    val activeTab by viewModel.selectedCrmTab.collectAsStateWithLifecycle()
    val leads by viewModel.crmLeads.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = if (isEnglish) "Agent & Agency Operations Room" else "Panel Operacyjny Agencji Nieruchomości",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = NeonCyan)
        )
        Spacer(modifier = Modifier.height(12.dp))

        // CRM Tabs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("PIPELINE", "ANALYTICS").forEach { tab ->
                val active = activeTab == tab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(if (active) NeonCyan.copy(0.15f) else SpaceSurface, RoundedCornerShape(10.dp))
                        .border(1.dp, if (active) NeonCyan else SpaceBorderWhite, RoundedCornerShape(10.dp))
                        .clickable { viewModel.selectedCrmTab.value = tab }
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (tab == "PIPELINE") {
                            if (isEnglish) "Leads & Bids Pipeline" else "Szpiedzy i Oferty Kupna"
                        } else {
                            if (isEnglish) "Performance Analytics" else "Analizy i Wykresy"
                        },
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (active) NeonCyan else Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (activeTab == "PIPELINE") {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(leads) { lead ->
                    CrmLeadCard(lead = lead, isEnglish = isEnglish)
                }
            }
        } else {
            // Analytics custom drawings
            CrmAnalyticsView(isEnglish = isEnglish)
        }
    }
}

@Composable
fun CrmLeadCard(lead: CrmLead, isEnglish: Boolean) {
    var stageState by remember { mutableStateOf(lead.stage) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(SpaceSurface, RoundedCornerShape(12.dp))
            .border(0.5.dp, SpaceBorderWhite, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = lead.name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Color.White))
                    Text(text = lead.propertyTitle, style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(0.5f)))
                }

                Box(
                    modifier = Modifier
                        .background(
                            when (stageState) {
                                "NEGOTIATION" -> CyberViolet.copy(0.2f)
                                "APPROVED_SCREENING" -> NeonEmerald.copy(0.2f)
                                else -> ElectricBlue.copy(0.2f)
                            },
                            RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = stageState,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = when (stageState) {
                                "NEGOTIATION" -> CyberViolet
                                "APPROVED_SCREENING" -> NeonEmerald
                                else -> ElectricBlue
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 8.sp
                        )
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = lead.detail, style = MaterialTheme.typography.labelSmall.copy(color = NeonCyan, fontWeight = FontWeight.Bold))
                Text(text = lead.contact, style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(0.4f)))
            }

            // Interactive Actions inside CRM
            if (stageState == "NEGOTIATION" || stageState == "SCHEDULED") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { stageState = "APPROVED_SCREENING" },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonEmerald.copy(alpha = 0.2f), contentColor = NeonEmerald),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.weight(1f).height(32.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(text = if (isEnglish) "Approve Tenant" else "Zatwierdź najemcę", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = { stageState = "COMPLETED" },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue.copy(alpha = 0.2f), contentColor = ElectricBlue),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.weight(1f).height(32.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(text = if (isEnglish) "Mark Sold" else "Oznacz sprzedane", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun CrmAnalyticsView(isEnglish: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Commission overview metrics
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(SpaceSurface, RoundedCornerShape(12.dp))
                    .border(0.5.dp, SpaceBorder, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Text(text = if (isEnglish) "AVG SALES PERIOD" else "ŚREDNI CZAS TRANSAKCJI", style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(0.4f), fontSize = 8.sp))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "18 Days / Dni", style = MaterialTheme.typography.bodyMedium.copy(color = NeonCyan, fontWeight = FontWeight.Bold))
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(SpaceSurface, RoundedCornerShape(12.dp))
                    .border(0.5.dp, SpaceBorder, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Text(text = if (isEnglish) "EST. COMMISSIONS" else "PROGNOZA PROWIZJI", style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(0.4f), fontSize = 8.sp))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "142,500 PLN", style = MaterialTheme.typography.bodyMedium.copy(color = CyberViolet, fontWeight = FontWeight.Bold))
                }
            }
        }

        // Custom pure Compose bar chart for average district demand
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(SpaceSurface, RoundedCornerShape(16.dp))
                .border(0.5.dp, SpaceBorderWhite, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = if (isEnglish) "MONTHLY SALES SPEED BY DISTRICT" else "TEMPO SPRZEDAŻY WG DZIELNIC (DNI)",
                    style = MaterialTheme.typography.labelSmall.copy(color = NeonCyan, fontWeight = FontWeight.Bold)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    val bars = listOf(
                        BarItem("Srod.", 12f, NeonCyan),
                        BarItem("Moko.", 16f, CyberViolet),
                        BarItem("Krzy.", 21f, ElectricBlue),
                        BarItem("Jeli.", 26f, NeonEmerald)
                    )

                    bars.forEach { bar ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            Text(
                                text = "${bar.valDays.toInt()}d",
                                style = MaterialTheme.typography.labelSmall.copy(color = Color.White, fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .width(36.dp)
                                    .height((bar.valDays * 4).dp)
                                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                    .background(bar.color)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = bar.name,
                                style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(0.6f))
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

data class BarItem(val name: String, val valDays: Float, val color: Color)


// ==========================================
// 6. SYSTEM SPEC & INTERACTIVE ARCHITECTURE CONSOLE (1-10 DELIVERABLES)
// ==========================================
@Composable
fun DeveloperConsoleScreen(isEnglish: Boolean) {
    var selectedSpecTab by remember { mutableStateOf("ARCH") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = if (isEnglish) "POLISPACE Enterprise Core Specifications" else "Specyfikacja Techniczna Projektu POLISPACE",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = NeonCyan)
        )
        Text(
            text = if (isEnglish) "Review high-scale architecture & deliverable design specs." else "Przeglądaj pełną architekturę i schematy bazodanowe.",
            style = MaterialTheme.typography.bodySmall.copy(color = Color.White.copy(0.4f))
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Horizontal sub-tabs for tech deliverables
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TechTabButton(label = "Architecture", active = selectedSpecTab == "ARCH") { selectedSpecTab = "ARCH" }
            TechTabButton(label = "PostgreSQL DB", active = selectedSpecTab == "DB") { selectedSpecTab = "DB" }
            TechTabButton(label = "Search Index", active = selectedSpecTab == "SEARCH") { selectedSpecTab = "SEARCH" }
            TechTabButton(label = "Spring API", active = selectedSpecTab == "API") { selectedSpecTab = "API" }
            TechTabButton(label = "Cloud Deploy", active = selectedSpecTab == "CLOUD") { selectedSpecTab = "CLOUD" }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Scrollable Interactive Output Board
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(SpaceSurface, RoundedCornerShape(12.dp))
                .border(1.dp, SpaceBorder, RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                when (selectedSpecTab) {
                    "ARCH" -> ArchitectureSpecView(isEnglish = isEnglish)
                    "DB" -> DatabaseSchemaSpecView()
                    "SEARCH" -> SearchEngineSpecView()
                    "API" -> SpringApiSpecView()
                    "CLOUD" -> CloudDeploymentSpecView()
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun TechTabButton(label: String, active: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(if (active) CyberViolet.copy(alpha = 0.2f) else SpaceSurfaceVariant, RoundedCornerShape(8.dp))
            .border(1.dp, if (active) CyberViolet else Color.Transparent, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(text = label, style = MaterialTheme.typography.labelSmall.copy(color = if (active) CyberViolet else Color.White, fontWeight = FontWeight.Bold))
    }
}

@Composable
fun ArchitectureSpecView(isEnglish: Boolean) {
    Text(
        text = if (isEnglish) "1. Enterprise Microservices Architecture" else "1. Architektura Mikrousługowa Systemu",
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = NeonCyan)
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = """
POLISPACE uses a high-scale microservices system deployed on Kubernetes. Below is the text-based architecture topology:

[ Kotlin Mobile ]    [ TypeScript Web CRM ]
       |                      |
       v                      v
[ --------- API Gateway (Spring Cloud / Envoy) --------- ]
                               |
       +-----------------------+-----------------------+
       |                       |                       |
[ User Service ]       [ Property Service ]    [ Search Service ]
       |                       |                       |
 [ PostgreSQL Auth ]    [ PostgreSQL+PostGIS ]    [ Elasticsearch ]
       |                       |                       |
       +-----------+-----------+-----------+-----------+
                   |                       |
            [ Redis Cache ]     [ RabbitMQ Message Bus ]
                   |                       |
                   v                       v
          [ AI Valuation Engine ]  [ Notification Engine ]
        """.trimIndent(),
        style = MaterialTheme.typography.bodySmall.copy(
            fontFamily = FontFamily.Monospace,
            color = Color.White.copy(alpha = 0.9f),
            lineHeight = 16.sp
        )
    )
}

@Composable
fun DatabaseSchemaSpecView() {
    Text(
        text = "2. PostgreSQL + PostGIS Geospatial Database Schema",
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = NeonCyan)
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = """
-- Active PostgreSQL Schema with PostGIS geospatial indexes
CREATE EXTENSION IF NOT EXISTS postgis;

CREATE TABLE agencies (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    license_no VARCHAR(50) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE properties (
    id VARCHAR(50) PRIMARY KEY,
    title_pl VARCHAR(200) NOT NULL,
    title_en VARCHAR(200) NOT NULL,
    price DECIMAL(15,2) NOT NULL,
    area_sqm DECIMAL(8,2) NOT NULL,
    rooms INT NOT NULL,
    energy_class CHAR(1),
    location GEOGRAPHY(Point, 4326), -- PostGIS Spatial Index
    agency_id VARCHAR(50) REFERENCES agencies(id)
);

CREATE INDEX idx_properties_spatial 
ON properties USING GIST(location);

CREATE TABLE bids (
    id SERIAL PRIMARY KEY,
    property_id VARCHAR(50) REFERENCES properties(id),
    bidder_name VARCHAR(100),
    amount DECIMAL(15,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
        """.trimIndent(),
        style = MaterialTheme.typography.bodySmall.copy(
            fontFamily = FontFamily.Monospace,
            color = Color.White.copy(alpha = 0.9f)
        )
    )
}

@Composable
fun SearchEngineSpecView() {
    Text(
        text = "3. Elasticsearch Properties Search Engine Integration",
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = NeonCyan)
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = """
// Elasticsearch Mapping Spec for Sub-second radius filters
PUT /polispace_listings
{
  "mappings": {
    "properties": {
      "id": { "type": "keyword" },
      "title": { "type": "text", "analyzer": "polish" },
      "price": { "type": "double" },
      "areaSqm": { "type": "float" },
      "pin_location": { "type": "geo_point" }, // For rapid spatial range queries
      "energyEfficiency": { "type": "keyword" }
    }
  }
}

// Sample Query: Find within 5km distance in Warsaw Center
POST /polispace_listings/_search
{
  "query": {
    "bool": {
      "must": { "match": { "title": "mieszkanie" } },
      "filter": {
        "geo_distance": {
          "distance": "5km",
          "pin_location": { "lat": 52.23, "lon": 21.01 }
        }
      }
    }
  }
}
        """.trimIndent(),
        style = MaterialTheme.typography.bodySmall.copy(
            fontFamily = FontFamily.Monospace,
            color = Color.White.copy(alpha = 0.9f)
        )
    )
}

@Composable
fun SpringApiSpecView() {
    Text(
        text = "4. Spring Boot REST Controllers API Specification",
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = NeonCyan)
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = """
@RestController
@RequestMapping("/api/v1/properties")
@CrossOrigin(origins = "*")
class PropertyController(private val service: PropertyService) {

    @GetMapping
    fun getAllProperties(
        @RequestParam(required = false) city: String?,
        @RequestParam(required = false) maxPrice: Double?
    ): ResponseEntity<List<PropertyDto>> {
        return ResponseEntity.ok(service.search(city, maxPrice))
    }

    @PostMapping("/{id}/bids")
    fun submitBid(
        @PathVariable id: String,
        @RequestBody bidRequest: BidRequest
    ): ResponseEntity<BidResponse> {
        val updatedBids = service.addBid(id, bidRequest)
        // Dispatches live updates via WebSocket channel
        webSocketService.broadcast("/topic/bids/" + id, updatedBids)
        return ResponseEntity.ok(updatedBids)
    }
}
        """.trimIndent(),
        style = MaterialTheme.typography.bodySmall.copy(
            fontFamily = FontFamily.Monospace,
            color = Color.White.copy(alpha = 0.9f)
        )
    )
}

@Composable
fun CloudDeploymentSpecView() {
    Text(
        text = "5. Multi-Region EU Cloud Topology & Kubernetes YAML",
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = NeonCyan)
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = """
apiVersion: apps/v1
kind: Deployment
metadata:
  name: polispace-property-service
  namespace: prod-polispace
spec:
  replicas: 5
  selector:
    matchLabels:
      app: property-service
  template:
    metadata:
      labels:
        app: property-service
    spec:
      containers:
      - name: spring-backend
        image: europe-west1-docker.pkg.dev/polispace/prod/property-service:v2.4
        ports:
        - containerPort: 8080
        envFrom:
        - secretRef:
            name: db-credentials-secret
        resources:
          limits:
            cpu: "2"
            memory: 4Gi
          requests:
            cpu: "500m"
            memory: 1Gi
        readinessProbe:
          httpGet:
            path: /actuator/health/readiness
            port: 8080
        livenessProbe:
          httpGet:
            path: /actuator/health/liveness
            port: 8080
        """.trimIndent(),
        style = MaterialTheme.typography.bodySmall.copy(
            fontFamily = FontFamily.Monospace,
            color = Color.White.copy(alpha = 0.9f)
        )
    )
}
