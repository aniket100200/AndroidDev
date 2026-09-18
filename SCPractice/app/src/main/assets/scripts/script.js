// 1. Declare your constants FIRST
const subjects = [
    "Maths", "Reasoning", "History", "Geography", "Polity",
    "Economics", "general_science", "Current_Affairs", "Computer_Science", "English"
];

const quizData = [{
    "subjectName": "Mathematics",
    "topics": [
        {
            "topicId": "math_01",
            "topicName": "Algebra",
            "questions": [
                {
                    "questionId": "q_01",
                    "questionText": "Solve for x: 2x + 5 = 15",
                    "options": ["3", "5", "10", "12"],
                    "correctAnswerIndex": 1
                }
            ]
        }
    ]
}];

// 3. Define the function
function loadQuizData(pageId = 0) {
    const subject = subjects[pageId];
    fetch(`data/${subject}.json`)
        .then(response => response.json())
        .then(data => {
            // Process the fetched data
            var topicList = document.getElementById("topicList");
            var currData = data;
            var topics = currData.topics;
            topics.forEach(topic => {
                var listItem = document.createElement("li");
                appendAnchorTag(topic.topicName, listItem);
                listItem.setAttribute("data-topic-id", topic.topicId);
                topicList.appendChild(listItem);
            });
        }).catch(error => {
            console.error("Error loading quiz data:", error);
        });
}

function appendAnchorTag(name, listItem) {
    var anchor = document.createElement("a");
    anchor.href = "#";
    anchor.textContent = name;
    listItem.appendChild(anchor);
}
