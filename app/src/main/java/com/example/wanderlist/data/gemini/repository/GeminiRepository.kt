package com.example.wanderlist.data.gemini.repository
import com.google.firebase.Firebase
import com.google.firebase.vertexai.type.HarmBlockThreshold
import com.google.firebase.vertexai.type.HarmCategory
import com.google.firebase.vertexai.type.SafetySetting
import com.google.firebase.vertexai.type.generationConfig
import com.google.firebase.vertexai.vertexAI
import kotlinx.serialization.json.Json

class GeminiRepository {
    private val generativeModel = Firebase
        .vertexAI
        .generativeModel(
            modelName = "gemini-2.0-flash-lite-001",
            generationConfig = generationConfig {
                temperature = 0f
            },
            safetySettings = listOf(
                SafetySetting(HarmCategory.HARASSMENT, HarmBlockThreshold.LOW_AND_ABOVE),
                SafetySetting(HarmCategory.HATE_SPEECH, HarmBlockThreshold.LOW_AND_ABOVE),
                SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, HarmBlockThreshold.LOW_AND_ABOVE),
                SafetySetting(HarmCategory.DANGEROUS_CONTENT, HarmBlockThreshold.LOW_AND_ABOVE),
            )
        )

    suspend fun generateQuests(displayName: String?): List<String>? {
        val prompt = "You will be provided with the following information:\n\nEstablishment Name: " +
                "${displayName}\n\nInstructions:\n\n0. Search online for the provided establishment name and identify its type " +
                "(e.g., convenience store, restaurant, park, etc.).\n" +
                "1. Generate a list of 5 unique quests specifically designed for the given establishment.\n" +
                "2. Each quest should be actionable and achievable within the establishment.\n" +
                "3. The quests should be engaging and interesting for users.\n" +
                "4. Output a list of strings, not a list of objects. The output should be bare and only the list so it's parseable. Do not include tick marks (```) at the beginning or end.\n" +
                "5. Ensure the quests are relevant to the establishment's type and name, based on the information found online.\n" +
                "6. Each quest should be under 75 characters in length.\n\n\n" +
                "Example Output:\n\n" +
                "[\n  \"Order and savor each of the establishment's signature coffee blends.\",\n  " +
                "\"Try three different pastries and rate them based on taste and presentation.\",\n  " +
                "\"Strike up a conversation with a barista and learn about their favorite coffee brewing method.\",\n  " +
                "\"Find a cozy spot and read a chapter from your favorite book while enjoying a beverage.\",\n  " +
                "\"Meet and chat with three other patrons at the establishment.\"\n]"


        val response = generativeModel.generateContent(prompt).text ?: ""

        // Convert the JSON string to a List<String> using kotlinx.serialization
        return Json.decodeFromString(response)
    }
}