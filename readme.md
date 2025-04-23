# WANDERLIST

## How to install
1. Download Android Studio
2. Clone repo
3. Open the repo and build

## Coding Practices
### **Documented Coding Standards**

Documenting the coding standards used by your group can improve the quality of the software that you write by making it more consistent between developers. Coding standards should describe conventions used by the team, such as where braces are positioned, naming conventions for classes, methods, variables, and documentation conventions. Many programming languages have several different published coding standards for that language. Your team may wish to adopt a published standard for the language of choice, rather than trying to develop your own. Upon the adoption of coding standards, all written code should be consistent with these standards. This can be proven by a link or a complete description of the standard being adopted.

### Coding Standards are based off of Kotlin's Official Conventions
#### https://kotlinlang.org/docs/coding-conventions.html
### Variable Naming:

#### Classes:
Should be nouns, starting with an uppercase letter, and use pascal case.
Example:
```
class Car
class ElectricVehicle
```
#### Variables:
Should start with a lowercase letter and use camel case. They should be descriptive and concise.

Example:
```
val userName: String = "JohnDoe"
var itemCount: Int = 0
```

#### Methods:
Method names should be verbs, starting with a lowercase letter, and use camel case. They should clearly indicate the method's action.

Example:
```
fun calculateArea() {}
fun displayMessage(message: String) {}
### Syntax Placement:
```
#### Curly Braces
The opening curly brace should be placed at the end of the line where the construct begins (class declaration, function declaration, etc.). The closing brace should be places on a separate line, aligning vertically with the beginning of the construct.
```
fun exampleFunction(condition: Boolean) {
    if (condition) {
        // Code to execute if condition is true
    } else {
        // Code to execute if condition is false
    }
}
```

### Horizontal whitespace﻿
- Put spaces around binary operators (`a + b`). Exception: don't put spaces around the "range to" operator (`0..i`).
- Do not put spaces around unary operators (`a++`).
- Put spaces between control flow keywords (`if, when, for, and while`) and the corresponding opening parenthesis.
- Do not put a space before an opening parenthesis in a primary constructor declaration, method declaration or method call.
```
class A(val x: Int)

fun foo(x: Int) { ... }

fun bar() {
    foo(1)
}
```

As a general rule, avoid horizontal alignment of any kind. Renaming an identifier to a name with a different length should not affect the formatting of either the declaration or any of the usages.

### Github Methologies:
#### PRs & Branch Naming
Create branches with your jira ticket and type of edit at the beginning.
- `WS-<jira #>-<type>/<description, with separator as “-”>`
- `Examples: WS-1-feat/create-new-button`, `WS-2-chore/update-gitignore`, `WS-2-fix/broken-button`

#### Issue Tracking
Create an issue along with your ticket/task on Jira. Create your branches and PRs based off that issue. Do this along with the naming conventions described above.

[Best Practices Jira Docs](https://wanderlistdev.atlassian.net/wiki/spaces/WS/pages/196609/Coding+Standards?atlOrigin=eyJpIjoiMjYxZWJhZmRlN2NjNDYzZmEyMzkyNjhhNzJiZTU3NTQiLCJwIjoiaiJ9)
