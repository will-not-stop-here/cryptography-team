# RushHour_Java
A CLI and GUI implementation of the Rush Hour puzzle game. Completed with two group members, Daphne and Emma. Code contributions are noted in the docstrings. 

This project was an excellent opportunity to learn about the challenges of completing a coding project with team members. 

Notable features of this project include: 
- GUI interface (\src\main\java\rushhour\view\RushHourGUI.java)
- CLI interface (\src\main\java\rushhour\view\RushHourCLI.java)
- Backtracking algorithm used for the "solve" button in the GUI, and "solve" command in the CLI 
- test code for all methods!
- adherence to the "Model View Controller" design pattern
- detailed docstrings for each method!
- highly detailed comments for all of the code

The CLI implementation of the Rush Hour game is one of the areas where I took ownership. 
Learning about all the various ways a user might input faulty commands, and creating a safe environment for the user to play were challenges I thoroughly enjoyed navigating. 
It was also quite rewarding to go through all of our code towards the end of the project, searching for any ways we might refactor our solutions to be more efficient. 

One of the biggest takeaways I have from this experience, is the importance of being absolutely meticulous with my code when I am writing the simpler, foundational code. 
We ended up running into a bug where moving a vehicle to the RIGHT would cause portions of the vehicle to disappear. 
I spent hours combing through the code to find the bug, only to discover that one of the first methods we wrote (rushhour.model.Vehicle.move(Direction dir)), had the classic error of (x,x) when we wanted (x,y).
A simple mistake that ended up costing hours! The silver lining of this bug-hunt was that I ended up discovering numerous other mistakes that might have gone unnoticed had I not combed through every line of code in the project, haha!

Thank you for taking a look at this project.
A big thank you to Emma and Daphne for their contributions as team members!

Lennard Szyperski

RushHour_Java
这是一个 Rush Hour 拼图游戏的 CLI 和 GUI 实现。与组员 Daphne 和 Emma 一起完成。代码贡献在文档字符串中有记录。

这个项目是学习与团队成员一起完成编码项目的挑战的绝佳机会。

这个项目的显著特点包括：

GUI 界面（\src\main\java\rushhour\view\RushHourGUI.java）
CLI 界面（\src\main\java\rushhour\view\RushHourCLI.java）
用于 GUI 中的 "solve" 按钮和 CLI 中的 "solve" 命令的回溯算法
所有方法的测试代码！
遵循 "Model View Controller" 设计模式
每个方法都有详细的文档字符串！
所有代码都有高度详细的注释
Rush Hour 游戏的 CLI 实现是我负责的领域之一。了解用户可能输入错误命令的各种方式，并创建一个安全的环境让用户进行游戏，是我非常喜欢处理的挑战。在项目结束时深入研究我们的所有代码，寻找任何可以重构以提高效率的方式，也是一件非常有成就感的事情。

从这次经验中我得到的最大启示是，在编写更简单、基础的代码时，细心严谨是多么重要。我们最终遇到一个 bug，将车辆移动到右侧会导致车辆的某些部分消失。我花了几个小时仔细查找代码中的错误，最终发现我们最初编写的其中一个方法（rushhour.model.Vehicle.move(Direction dir)）出现了经典错误，使用了 (x, x) 而我们需要的是 (x, y)。一个简单的错误最终花费了几个小时！这次错误追踪的一点好处是，我最终发现了许多其他可能被忽视的错误，如果我没有逐行查看项目中的每一行代码，可能就会被忽略，哈哈！

感谢您查看这个项目。对于团队成员 Emma 和 Daphne 的贡献，表示衷心感谢！

Lennard Szyperski