# COMP9336 Mobile Data Networking

Assignment: Experimental Study of an Enterprise-Wide Wireless LAN
=====

Introduction
-----
To provide ubiquitous enterprise-wide wireless connectivity, many corporations are rolling out large-scale wireless LANs to cover an entire campus. Thousands of interconnected WiFi access points (APs) are deployed all over the campus to provide rich on-line experience for the employees and visitors. UNSW is one such institution with close to 4,000 APs deployed in its Sydney campus. The interconnected campus-wide wireless LAN is called UniWide. Everyday, UniWide is used by thousands of students and employees from hundreds of different locations in the campus, both indoor and outdoor. Unfortunately, the true performance of UniWide at every single location of the campus is difficult to predict. The only way to obtain such a network performance map is to carry out a comprehensive measurement campaign. This assignment involves designing and executing an experimental study to generate a wireless performance map of UniWide.

Link-layer Analysis: 
-----
Students should visit as many locations as possible within the campus and collect the following data for UniWide at each location:  
1. 802.11 protocol: identify which protocols/standards, e.g., 802.11a/b/g/n/ac, are available   
2. Signal strength: obtain signal strength statistics   
3. Data Rate: obtain 802.11 data rate statistics  
4. AP Density: record coverage density for the location, i.e., how many APs are covering the location   

Mobility Analysis: 
-----
Students should measure the following mobility-related performance by measuring data while moving from one location to another within the campus:   
1. L4 connectivity: Is TCP connection lost or preserved while moving from one location to another. Ideally, it would be good to identify some pairs of location between which TCP connection is preserved, and some other pairs for which TCP connection is lost.   
2. L3 handoff: For location pairs experiencing TCP connection loss, measure the time it takes to obtain a new IP number. The time from losing the IP address to the moment a new IP address is obtained is called the IP handoff delay. Collect statistics for IP handoff delay.  
3. L2 handoff: While moving from one location to another, record AP changes. The time it takes to disassociate from an AP and re-associate to a new one is called L2 handoff delay. Collect L2 handoff delay statistics.  

Results:
-----
![](https://github.com/BriseKael/COMP9336/blob/master/img/AP%20Density.png)
![](https://github.com/BriseKael/COMP9336/blob/master/img/Data%20Rate.png)
![](https://github.com/BriseKael/COMP9336/blob/master/img/Signal%20Strength.png)
![](https://github.com/BriseKael/COMP9336/blob/master/img/Protocols%20Use.png)


Authors
-----
![](https://github.com/BriseKael/COMP9336/blob/master/img/QuanYinXiChen.jpg)  
Quan Yin  
Xi Chen

2017 S2 COMP9336 DN
Assignment 20/20
