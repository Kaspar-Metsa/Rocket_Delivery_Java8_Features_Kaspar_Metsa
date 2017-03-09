This is a TTU university project completed by me - a Java 8 Rocket Delivery System

Please test application by downloading and running it

Requirements for the Java 8 project: (all these features are working)
==============
* This assignment has to use Java 8 functionalities like in example like Streams, Lambdas, Functional Interfaces (for example Predicate<T>, Method References, Optionals, correctly synchronized threads with Wait/Notify concept. 
* This assignment must demonstrate these principles:
  * Encapsulation - this means that all business logic needs to encapsulated in one place. When you create a post office, the post office should not know anything that there are different types of rockets. All components should be as independent as possible and the business logic should be hidden inside classes. Also If you have a business requirement that a regular rocket can't go to Venus or Mars, then the rocket itself should know where it can fly, the post office that receives packages from rocket should not know it.
  * Composition
  * Dependency injection
  * Polymorphism
  * Choose and use functional interfaces, method references and lambdas
  * Write working and effective multithreaded application - some parts of it have to work at the same time.
  
  
* This assignment has to use threads that have to work simultaneously. The business logic should be hidden inside classes - The post office should not know at all that there exist different types of rockets.

**1st part**
Every planet and moon has a post office where the post office can:

1.	Load the packets to the rocket.

2.	Off-load the packets from the rocket.

3.	"Service" the rocket.

⋅⋅⋅a.	Fuel-tanking- Servicing includes increasing the fuel of the rocket to the maximum. 
  
⋅⋅⋅b.	Only on the planets Jupiter and Neptune: Servicing also includes exchanging the cosmic ray indicator(9th part) if the indicator has equal to or less than 2 starts.



**2nd Part**
The rocket uses dark matter as fuel which the rocket gets from "service" in the post office of every planet and moon. The exact size of the dark matter fuel tank is not known but what is known:

1.	With a completely full fuel tank, the rocket can do 5 starts (every start uses 20% of the fuel)

2.	You can think that 100% fuel tank consists of 100 units of fuel.

3.	After tanking the rocket 5 times, the rocket must be serviced (but it can be serviced more often than that).

4.	It is important that the rocket counts itself how many units of fuel it received during the fuel-tanking and the rocket also needs to keep a count of how much fuel it currently has.

**3rd Part**
To the 2 planets Mercury and Venus post can only be delivered with a special heat-resistant rocket(My comment: Probably has to be done with one class extending another)

1.	These rockets consume more fuel: Every start consumes 25% of total fuel

2.	The start from Mercury and Venus consumes even more fuel: Start from Mercury and Venus consumes 50% fuel.

**4th Part**
The rocket can only carry a maximum of 100kg of packages.

1.	The heat-resistant rocket can only carry a maximum of 80kg of packages.

**5h Part: Package**
Every package that is going to be transported has these attributes:

1. The place it is transported from

2. The place it is transported to

3. Weight(between 1-80kg)

**6th Part: Post offices**
Every Post Office keeps count of received packages. At any moment you can ask from post office:
1.	How many packages has it received?

2.	What is the total weight of all the received packages?

3.	What is the average weight of all the received packages?

4.	The list of planets where these packages have come from to this post office?

5.	You can ask the number of packages according to your specific criteria, examples:(My comment: Probably has to be done with Java 8 Streams)

⋅⋅⋅a.	How many packages have been sent from Pluto?
⋅⋅⋅b.	How many packages over 7kg have been sent to this post office?
⋅⋅⋅c.	How many packages under 60kg have been sent from Venus?
⋅⋅⋅d.	NB! This does not mean that you create 3 methods for these 3 examples but 1 method that can solve these 3 questions if you give requirements for input - PLEASE SEE EXAMPLE.(My comment: PLEASE SEE RENTALSYSTEM CLASS IN EXAMPLE, THIS HAS TO BE DONE EXACTLY IN THE SAME WAY - YOU MUST USE JAVA 8 LAMBDAS, FUNCTIONAL INTERFACES(PREDICATES), OPTIONALS, STREAMS, METHOD REFERENCES, SYNCRONIZED THREADS WITH WAIT/NOTIFY)

**7th Part: Time to travel from one planet to other**
1.	The time to travel from any planet/moon to another planet/moon is always 15ms, the distance does not matter.

**8th Part: Starting the system and rules**
1.	When the system starts create 20 regular rockets and 5 heat resistant rockets.

2.	Create a separate thread that only creates packages. For every packet it RANDOMLY assigns:
⋅⋅⋅a.	Where this package came from (source post office)
⋅⋅⋅b.	Where this package is going to (destination post office)
⋅⋅⋅c.	Weight of the package
⋅⋅⋅d.	...And the thread places the package to the back of the queue of the source post office.
⋅⋅⋅e.	There is 3ms between creating every package. Total of 1500 packages are created.

3.	All rockets need to work at the simultaneously - at the same time(My comment: I think it has to be done with Threads that Use/Notify)

4.	Only one rocket can be "service"-d in a post office, other rockets need to wait if the  arrive at the same time this rocket is being "service"-d.(My comment: This probably has to be done with Wait/Notify concept with threads)

5.	No rocket can start without a package - every rocket must always have a package except when you need to fly to service station to exchange the cosmic ray indicator(9th part).

6.	Regular rocket may not accept packets that are meant to be delivered to Mercury or Venus.

7.	Heat-resistant rocket only accept packages that are delivered from or to Mercury or Venus.

**9th Part - Cosmic Ray Indicator**
1.	The cosmic ray indicator only lasts 25 starts, after 25 starts it needs to be exchanged:
⋅⋅⋅a.	The indicator is never exchanged before it only has 2 starts or less left(So for the example of the rocket's indicator has 22 starts with the indicator, it is not exchanged, at 23-25 starts it must be exchanged)
⋅⋅⋅b.	The cosmic ray indicator can only be exchanged in Jupiter or Neptune.

**10th Part - Additional Requirements for logistics**
1.	One rocket can carry multiple packages if the total weight of all packages carried in 1 rocket is less or equal to the maximum weight the rocket can carry (Please see 4th part).

2.	One rocket can pick up and carry packages that are meant for multiple planets.
⋅⋅⋅a.	This is why rockets don't always off-load all the packages in the post office, just those packages are off-loaded that are needed on that planet the rocket is currently visiting.

3.	Rocket also takes packages if it already has some packages, but it has enough room to carry some more:
⋅⋅⋅a.	In this case it prefers packages, that are being sent to the same planet that some other packages the rocket is already carrying, instead of packages that are being sent to a different planet.
⋅⋅⋅b.	Example: Rocket picks up 2 packages from Venus planet that need to be delivered to Earth planet and 1 package from Venus planet that need to be delivered to Mars planet. It delivers 1 package to Mars. Now on Mars, the  rocket has additional room so it picks up another package from Mars. If Mars has any package that is meant to go to Earth, it prefers that package because it goes there next anyway. If there is no package that is meant to be delivered to Earth, it takes any other package that adds another destination for the rocket. The rocket continues to go to Earth.

4.	Because the length of travel from all planets to all planets is the same 15ms, then distance in solar system is not important at all(you can deliver a package from Io to Earth and then to Mars).

**11th Part - Ending the work**
1.	In the beginning(first ~1000 packages) there probably isn't a problem with not having enough packages for every planet. In the end of the work, there might be a problem that some planets have packages that are not yet delivered, but rockets are waiting on other planets where there are no packages. This problem is because we have a requirement that no rocket can start without a package.

2.	This is why you need to create a "post round" - at least 1 rocket travels all the planets until all packets are delivered. You can either do it by creating a new rocket or using an existing one. All rockets do not have to do it. A regular rocket still can't travel to Venus or Mercury.

