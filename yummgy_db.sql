drop database if exists yummgy_db;
create database yummgy_db;
use yummgy_db;

CREATE TABLE users (
    user_id INT PRIMARY KEY 
				AUTO_INCREMENT,
    yum_username VARCHAR(255) NOT NULL,    
    yum_password VARCHAR(255) NOT NULL
   
);

insert into users(yum_username, yum_password) 
	values('BrionneB', "CogniJump23");
insert into users(yum_username, yum_password) 
	values('BryanD', "CogniJump23");
insert into users(yum_username, yum_password) 
	values('AbdulM', "CogniJump23");
insert into users(yum_username, yum_password) 
	values('GrantS', "CogniJump23");

CREATE TABLE recipe (
    recipe_id INT PRIMARY KEY 
				AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    prep_time INT,
    ingredients MEDIUMTEXT NOT NULL,
    directions MEDIUMTEXT NOT NULL,
    food_image_url VARCHAR(255),
    author INT,
    FOREIGN KEY (author) REFERENCES users(user_id)
);

#Insert Recipes

insert into recipe(title, prep_time, ingredients, directions, food_image_url, author)
	values('Beef Burgundy', 30, 
			'8 slices thick-cut bacon, chopped  1 (4- to 5-lb) boneless chuck roast, cut into 1 1/2-inch cubes  2 tsp. kosher salt, divided, plus more to taste  1 1/2 tsp. ground black pepper, divided  1 medium yellow onion, chopped  1 stalk celery, chopped  2 tbsp. tomato paste  1/4 c. all-purpose flour  6 cloves garlic, finely chopped  1 (750-mL) bottle red wine  1 (14.4-oz.) bag frozen pearl onions  1 tsp. chopped fresh thyme  1 bay leaf  4 peeled carrots, cut into 1-inch pieces  3 c. beef broth  1 pound button mushrooms, stems trimmed  Mashed potatoes, to serve  Chopped fresh parsley, to serve (at least 2 tbsp)',  
'1: Preheat the oven to 325°F.  2: Place a large Dutch oven over medium heat and add the bacon. Cook until crisp, 5 to 7 minutes. Using a slotted spoon, remove the bacon to a paper towel-lined plate, keeping the bacon drippings in the pot.  3: Pat the beef dry and season it with 1 1/2 teaspoons of salt and 1 teaspoon of pepper. Increase the heat to medium-high and brown the beef, in batches, on all sides, 4 to 5 minutes per batch. Remove the meat to a plate set aside. If there is a lot of oil left, pour off all but 2 tablespoons, reserving the rest for the mushrooms.  4: Add the onion and celery to the pot, and cook until the vegetables are tender and golden, 2 to 3 minutes. Stir in the tomato paste, flour, garlic, the remaining 1/2 teaspoon of salt, and the remaining 1/2 teaspoon of pepper, and cook for another 1 to 2 minutes. Pour in the wine, whisking to combine, and bring to a boil. Add the pearl onions, thyme, bay leaf, and carrots. Return the beef and half of the bacon to the pot. Pour in enough broth to just cover the meat and bring to a simmer. Cover and place in the oven. Cook until the beef is tender, 1 1/2 to 2 hours.  5:Meanwhile in a large skillet, melt 2 tablespoons of reserved cooking fat over medium heat. Add the mushrooms in one layer and cook, without moving, until golden, about 5 minutes. Flip them over and continue cooking until golden brown all over, 5 to 7 minutes more. Remove from the heat.  6: Skim off any excess fat from the top of the stew and stir in the sautéed mushrooms. Taste for salt. Serve the stew over mashed potatoes, topped with parsley and the remaining bacon.', 'https://www.thecookierookie.com/wp-content/uploads/2018/12/easy-beef-bourguignon-recipe-2-of-6-735x1103.jpg', 1 );

insert into recipe(title, prep_time, ingredients, directions, food_image_url, author)
	values('Cranberry Curd Tart', 300, 
			'Cranberry Filling:  250 g or 1 ¼ cups sugar  510 g fresh cranberries  180 g or ¾ cups orange juice  Zest from 2 oranges  5 large eggs  113 g or ½ cup unsalted butter  Oatmeal Crust:  220 g or 2 cups quick cooking oats or rolled oats  30 g or ¼ cup all purpose flour  30 g or ¼ cup powdered sugar  1 tsp ground cinnamon  Pinch fine sea salt  ½ cup unsalted butter softened',  
'Make the filling:  1: Set the cranberries, orange juice, orange zest and sugar in a large pot over medium heat. Cook for about 15-20 minutes, until most of the cranberries have burst and all are soft. Turn off the heat.  2:
Use an immersion blender (or transfer the mix to a blender or food processor) to blend the cranberries into the juice. Through a fine mesh sieve, pour the puree into a bowl (or back into the pot if you used a blender). Press to squeeze out all the juice from the berries.  3: Crack all the eggs into a bowl, and one by one add them to the mix, whisking as you do (we don’t want the heat of the puree to start cooking the eggs before they are whisked in). Once all the eggs are mixed, whisk them with the puree very well.  4: Pour the curd back into the pot (if you had it in a bowl) and set it over medium low heat. Cook until the curd thickens and covers the back of a spoon. On a thermometer it should reach 165 F. The curd will thicken on the bottom first, be sure to stir it every 30 seconds so it doesn’t burn on the bottom.  5: Set the butter in a large bowl and set a fine mesh sieve (you can reuse the one you used earlier, just rinse it well) over the butter. Pour the curd through the sieve, pressing to extract all the juice. This will remove any unwanted cooked egg white bits.
Stir the butter into the curd until it fully melts. At this point you can transfer to an airtight container and store in the fridge for a few hours or up to a week. You can also freeze the curd for 2 months.  Make the crust:
1: In a food processor, add the oats, flour, sugar, cinnamon and sea salt. Process on high until the oatmeal is finely ground - about 2-4 minutes.  2: Add the butter in slices then run the mixer on low until the dough starts to come together in a sticky, shapeless ball. Scrape the bowl to incorporate all the flour and run to ensure it’s well mixed.  3: Press the crust into the bottom of a 10” tart pan with a removable bottom. Take your time to even out the bottom layer and press it up the sides. Use the bottom of a measuring cup dusted with powdered sugar to help you press it into shape.  4: Chill the crust in the freezer for about 10 minutes. Preheat the oven to 350 F.  5: Lay a sheet of parchment paper over the crust and fill the crust with pie weights or dried beans. Bake the crust for 25 minutes.  6: Use the parchment paper as a sling and lift the weights out. Gently dock the bottom of the crust by pricking it with a fork.  7: Pour the cranberry curd and smooth into an even layer. Set the tart in the oven for 20-22 minutes, until it’s matte colored all over, the sides don’t move when jiggled but there’s a slight jiggle in the center of the tart.  8: Chill first at room temperature, then set in the fridge to chill for 4-8 hours, or up to 2 days. Serve cold and store leftovers in the fridge.', 'https://buttermilkbysam.com/wp-content/uploads/2023/11/cranberry-curd-tart-3.webp', 2 );

insert into recipe(title, prep_time, ingredients, directions, food_image_url, author)
	values('Snowman Cheese Ball', 255, 
			'2 (8-ounce) packages cream cheese, softened  1 (8-ounce) package shredded pepper Jack cheese  1 tablespoon salt-free Cajun seasoning, such as T-Don’s Cajun Goods No Salt All Purpose Blend  1 (1-ounce) packet ranch dressing mix  10 whole peppercorns  1 baby carrot  1 jalapeno pepper with stem  crackers for serving',  
'Mix cream cheese, pepper Jack cheese, Cajun seasoning, and ranch dressing mix together in a large bowl; divide mixture to form one small cheese ball for the head and one larger cheese ball for the body. Wrap and chill in the refrigerator for 4 hours or up to overnight before serving.  Place larger cheese ball on a serving plate for body. Place smaller cheese ball on top for head. Slice the tip off baby carrot; press into cheese ball for nose. Use whole peppercorns for mouth, eyes, and buttons. Cut the top, with stem, from a jalapeno, and place on top for a hat. Serve with crackers.', 'https://www.allrecipes.com/thmb/yhyIJtRuYauEo4pCWx8hfE5c6ns=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/8406012_Snowman-Cheese-Ball_Chef-Mo_4x3-f8ca1d827cc54afd85308562d30fde62.jpg', 3 );

insert into recipe(title, prep_time, ingredients, directions, food_image_url, author)
	values('Gingerbread Bundt Cake', 105, 
			'baking spray with flour, such as Baker\'s Joy® 2 1/2 cups all-purpose flour  1 tablespoon ground ginger  2 teaspoons ground cinnamon  1 teaspoon baking powder  3/4 teaspoon salt  1/2 teaspoon ground nutmeg  
1/4 teaspoon baking soda  1/4 teaspoon ground cloves  1/4 teaspoon ground allspice  1/2 cup unsalted butter, softened  1/4 cup vegetable oil  1 cup firmly packed dark brown sugar  3/4 cup white sugar  3 large eggs, at room temperature  2/3 cup unsulphured molasses  2 teaspoons vanilla extract  1 cup full fat sour cream, at room temperature  powdered sugar for dusting on top (optional)',  
'1: Preheat the oven to 350 degrees F (180 degrees C). Spray a 10-cup Bundt pan with a baking spray containing flour (such as Baker\'s Joy®).  2: Whisk together flour, ginger, cinnamon, baking powder, salt, nutmeg, baking soda, cloves, and allspice in a bowl until combined.  3: Combine butter, oil, brown sugar, and white sugar in a large bowl. Best with an electric mixer until light and fluffy, about 3 minutes. Add in eggs, 1 at a time, beating well after each addition. After last egg is added, increase mixer speed to medium-high and beat until mixture has doubled in volume, about 5 minutes.  4: Mix in molasses and vanilla. Reduce mixer speed; mix in half of dry ingredients until just combined. Add in sour cream and mix until just combined. Pour in remaining dry ingredients and mix until just combined.  5: Pour batter evenly into prepared Bundt pan, then place pan on a baking sheet. 6: Bake in the preheated oven until a bamboo skewer inserted into the center of the cake comes out clean, 60 to 70 minutes. Allow cake to cool in pan for 20 minutes before removing to a wire rack to cool completely. Sprinkle with powdered sugar before serving, if desired.', 'https://www.allrecipes.com/thmb/7dH1R0CNwHqfPQjNuHlYy2OKl3A=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/8364140_Gingerbread-Bundt-Cake_Kim_4x3-f6cf50d6b2454c8d9dc538ffc16773f4.jpg', 4 );

insert into recipe(title, prep_time, ingredients, directions, food_image_url, author)
	values('Loaded Cheesy Green Bean Casserole Recipe', 45, 
			'1 pound green beans trimmed  10.5 ounces condensed Cream of Mushroom soup (1 can)  1 cup milk 1 teaspoon Worcestershire sauce  1 teaspoon garlic powder  1 teaspoon ground black pepper  2 cups freshly shredded cheddar cheese divided  4 slices crumbled cooked bacon  1½ cups french fried onions',  
'1: Preheat the oven to 350°F. Spray a 1-quart casserole dish with cooking spray and set aside.  2: In a large pot of boiling water, add the green beans and cook for 3 minutes, until bright green. Drain, then place in an ice bath to cool. Transfer to a large bowl and set aside.  3: Meanwhile, in a saucepan set over medium heat, whisk together the soup, milk, Worcestershire sauce, garlic powder, and pepper. Bring the mixture to a simmer, then whisk in 1 cup of the cheese until completely incorporated. Pour over the green beans and toss to coat, then transfer to a casserole dish. 4: Top with remaining cheese, then bake for 20 minutes, until warmed through and bubbling around the edges.  5: Top the casserole with fried onions and the crisp bacon and bake for an additional 5 minutes.', 'https://www.thecookierookie.com/wp-content/uploads/2021/09/loaded-green-bean-casserole-recipe-5-768x1152.jpg', 1 );

insert into recipe(title, prep_time, ingredients, directions, food_image_url, author)
	values('Bûche de Noël', 90, 
			'2 cups heavy cream  ½ cup confectioners\' sugar  ½ cup unsweetened cocoa powder  1 teaspoon vanilla extract  6 egg yolks  ½ cup white sugar  ⅓ cup unsweetened cocoa powder  1 ½ teaspoons vanilla extract  ⅛ teaspoon salt  6 egg whites  ¼ cup white sugar  confectioners\' sugar for dusting',  
'1: Preheat oven to 375 degrees F (190 degrees C).  Line a 10 x 15 inch jellyroll pan with parchment paper.  In a large bowl, whip cream, 1/2 cup confectioners\' sugar, 1/2 cup cocoa, and 1 tsp vanilla until thick and stiff. Refrigerate.  2: In a large bowl, use an electric mixer to beat egg yolks with 1/2 cup sugar until pale in color, light and frothy. Blend in 1/3 cup cocoa, 1 1/2 teaspoons vanilla, and salt.  3: In large glass bowl, using clean beaters, whip egg whites to soft peaks. Gradually add 1/4 cup sugar, and beat until whites form stiff peaks.  4: Immediately fold the yolk mixture into the whites.  5:Spread the batter evenly into the prepared pan.  6: Bake for 12 to 15 minutes in the preheated oven, or until the cake springs back when lightly touched.  7: Dust a clean dishtowel with confectioners\' sugar. Run a knife around the edge of the pan, and turn the warm cake out onto the towel. Remove and discard parchment paper.  8: Starting at the short edge of the cake, roll the cake up with the towel. Cool for 30 minutes.  9: Unroll the cake, and spread the filling to within 1 inch of the edge.  10: Roll the cake up with the filling inside. Place seam side down onto a serving plate, and refrigerate until serving.  11: Enjoy!', 'https://www.allrecipes.com/thmb/eaN37UJXhjP8WaQNHXbLGXlDAqs=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/6165525_Buche-de-Noel-4x3-1-abd236d8dd134ca5ade3d47dfd9260e7.jpg', 2 );

insert into recipe(title, prep_time, ingredients, directions, food_image_url, author)
	values('Mom\'s Potato Latkes', 30, 
			'3 cups shredded potato  ¼ cup grated onion  2 large eggs, beaten  6 saltine crackers, or as needed, crushed  ½ teaspoon salt, or more to taste  ¼ teaspoon ground black pepper  ½ cup vegetable oil, or as needed',  
'1: Mix potato, onion, eggs, crushed crackers, salt, and pepper together in a large bowl.  2: Heat 1/4 inch oil in a heavy skillet over medium-high heat.  3: Drop spoonfuls of the potato mixture, first pressing potato mixture against the side of the bowl to remove excess liquid, into the hot oil; slightly flatten the latkes into the oil with the back of your spoon so they are an even thickness.  4:Cook in batches in the hot oil until browned and crisp, 3 to 5 minutes per side. Drain latkes on a paper towel-line plate. ', 'https://www.allrecipes.com/thmb/gqG5w5U2cb0GKZhzWqj9vuEARvs=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/1423828-e7c9604aac204fe8994937e8782d42c8.jpg', 3 );

insert into recipe(title, prep_time, ingredients, directions, food_image_url, author)
	values('Perfect Crab-Stuffed Mushrooms', 40, 
			'2 tablespoons butter  2 tablespoons minced green onion  1 cup cooked crabmeat, finely chopped  ½ cup dry bread crumbs  ¼ cup shredded Monterey Jack cheese  1 egg, beaten  1 teaspoon lemon juice  ½ teaspoon dried dill weed  ½ cup butter, melted  1 ½ pounds fresh button mushrooms, stems removed  ½ cup shredded Monterey Jack cheese  ¼ cup dry white wine',  
'Preheat oven to 400 degrees F (200 degrees C).  2: Melt 2 tablespoons butter in a skillet; cook and stir green onion until softened, about 2 minutes. Transfer green onion to a bowl. Stir in crabmeat, bread crumbs, 1/4 cup Monterey Jack cheese, egg, lemon juice, and dill weed until well mixed.  3: Pour 1/2 cup melted butter in a 9x13-inch baking dish; turn mushroom caps in butter to coat. Fill mushroom caps with the crab mixture and sprinkle with remaining 1/2 cup Monterey Jack cheese. Pour white wine into baking dish.  4: Bake in preheated oven until cheese is melted and lightly brown, 15 to 20 minutes.', 'https://www.allrecipes.com/thmb/-lXuY8TF1OcKW-1v4vic5EHYyyE=/750x0/filters:no_upscale():max_bytes(150000):strip_icc():format(webp)/5939577-perfect-crab-stuffed-mushrooms-Mackenzie-Schieck-4x3-1-6715b79239274a5e99c473f8d397814f.jpg', 4 );
    

CREATE TABLE favorites (
    favorites_id INT PRIMARY KEY 
				AUTO_INCREMENT,
	user_id INT,
    recipe_id INT,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (recipe_id) REFERENCES recipe(recipe_id)     
);
       
    
#Insert User_show records
insert into favorites(user_id, recipe_id)
	values(1, 2	);
insert into favorites(user_id, recipe_id)
	values(1, 8	);
insert into favorites(user_id, recipe_id)
	values(2, 3	);
insert into favorites(user_id, recipe_id)
	values(2, 5	);
insert into favorites(user_id, recipe_id)
	values(3, 7	);
insert into favorites(user_id, recipe_id)
	values(3, 2	);
insert into favorites(user_id, recipe_id)
	values(4, 6	);
insert into favorites(user_id, recipe_id)
	values(4, 1	);
insert into favorites(user_id, recipe_id)
	values(4, 4	);
insert into favorites(user_id, recipe_id)
	values(1, 1	);
insert into favorites(user_id, recipe_id)
	values(1, 5	);
insert into favorites(user_id, recipe_id)
	values(2, 1	);
insert into favorites(user_id, recipe_id)
	values(2, 7	);
insert into favorites(user_id, recipe_id)
	values(1, 7	);
insert into favorites(user_id, recipe_id)
	values(4, 7);

    