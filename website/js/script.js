//main html variables
const mainBody=document.getElementById("mainBodyID");
const width=window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;

//menu bar variables
const menuToggle=document.getElementById("menu-toggle");
const menuNav=document.getElementById("navbar");
const sticky=menuNav.offsetTop;
const menuNavLinks = menuNav.getElementsByTagName("a");


//gallery variables
const nextBtn=document.getElementById("nextBtn");
const prevBtn=document.getElementById("prevBtn");

//contact me dialog variables
const userEmail=document.getElementById("user-email");
const subscribeBtn=document.getElementById("subscribeBtn");
const openContactDialog=document.getElementById("open-label");
const exitContactDialog=document.getElementById("message-container-heading");
const contactDialog=document.getElementById("message-container");
const contactForm=document.getElementById("contact-form");

//subscribe dialog variables
const modal=document.getElementById("modal");
const exitButton=document.getElementById("exit");

const video = document.getElementById("preview");



const toggleMenu = () => {
  if (window.innerWidth < 1000) {
    menuNav.classList.toggle("menu-toggle");
    menuNav.classList.add("sidenav");
  }
}

const menuBarLinksActive = () => {
  if (window.innerWidth > 1000) {
    for (let i = 0; i < menuNavLinks.length; i++) {
      menuNavLinks[i].addEventListener("click", function() {
        let current = document.getElementsByClassName("current");
        current[0].className = current[0].className.replace(" current", "");
        this.className += " current";
      });
    }
  }
}


const subscribeDialogDisplay = () => {
  if (document.getElementById("user-email").value.length == 0) {
    return;
  } else {
    email = userEmail.value;
    localStorage.setItem('email', email);
    modal.style.display = "block";
    document.getElementById("output-email").innerHTML = localStorage.getItem("email");
  }
}

const dialogExit = () => {
  modal.style.display = "none";
}

const contactDialogDisplay = () => {
  contactDialog.style.display = "block";
}

const contactDialogExit = () => {
  contactDialog.style.display = "none";
}

const menuNavMobileDisplay = () => {

  /**
   * This if/ else statments help with deattaching the #sidenav class on window resize, without
   * these statements the class will stay attatched on resize and the desktop styling won't be applied
   * Logic was taken from stackoverflow
   */
  if (window.attachEvent) {
    window.attachEvent('onresize', function() {
      alert('attachEvent - resize');

      if (width >= 600) {
        menuNav.classList.remove("sidenav");
      }

      if (width <= 599) {
        menuNav.classList.remove("sidenav");
      }
    });
  }

  else if (window.addEventListener) {
    window.addEventListener('resize', function() {
      if (width >= 600) {
        menuNav.classList.remove("sidenav");
      }

      if (width <= 599) {
        menuNav.classList.remove("sidenav");
      }
    }, true);
  }

  else {
    //The browser does not support Javascript event binding
  }
}

const contactFormSubmission = () => {
  document.getElementById("contact-form-submission-received").classList.add("fade");
  contactForm.reset();
  preventDefault();
  return false;
}

const gallery = () => {
  let slideIndex = 1;
  showDivs(slideIndex);

  function plusDivs(n) {
    showDivs(slideIndex += n);
  }

  function showDivs(n) {
    let i;
    let x = document.getElementsByClassName("slides");
    let y = document.getElementsByClassName("dot");

    if (n > x.length) {
      slideIndex = 1;
    }

    if (n < 1) {
      slideIndex = x.length;
    }

    for (i=0; i < x.length; i++) {
      x[i].style.display = "none";
      y[i].classList.remove("active");
    }

    x[slideIndex - 1].style.display = "inline";
    y[slideIndex - 1].classList.add("active");
  }

  setInterval(plusDivs.bind(null, 1), 3000);

  nextBtn.addEventListener("click", function() {
    showDivs(slideIndex += 1);
  }

  );

  prevBtn.addEventListener("click", function() {
    showDivs(slideIndex -= 1);
  }

  );
}

const typeWriter = () => {
  const dataText=["Travel safetly and quickly",
  "To your favourite destination",
  "Carpooling made easy"];

  function typeWriter(text, i, fnCallback) {
    if (i < (text.length)) {
      document.querySelector("h1").innerHTML=text.substring(0, i + 1)+'<span class="text-writer-cursor" aria-hidden="true"></span>';

      setTimeout(function() {
        typeWriter(text, i + 1, fnCallback)
      }, 100);
    }

    else if (typeof fnCallback=='function') {
      setTimeout(fnCallback, 700);
    }
  }

  function StartTextAnimation(i) {
    if (typeof dataText[i] == 'undefined') {
      setTimeout(function() {
        StartTextAnimation(0);
      }, 20000);
    }

    if (dataText[i] != undefined) {
      if (i < dataText[i].length) {
        typeWriter(dataText[i], 0, function() {
          StartTextAnimation(i + 1);
        });
      }
    }
  }

  StartTextAnimation(0);
}

const playVideo = () => {
  video.play;
  video.muted = true;
}

const fetchJsonApi=()=> {
  /**
   * The XML example for the labs had to be adapted since i was making multiple requests to different URL's
   * and was using WET code for repeating the XML objects.Since github's Json API is seperate for fetching
   * commits, i had to use Promise instead since its much for faster and efficent and won't cause rate
   * limiting issues.
   */
  const urls = [
    'https://api.github.com/user/repos?access_token=2f4d73f54c9e54874633817122a91daf70ed8875'
  ];

  function getAll(urls) {
    const promises = urls.map(url => {
      return fetch(url)
        .then(response => response.json())
        .catch((err) => console.log(err))
    })

    return Promise.all(promises);
  }

  getAll(urls)
    .then(txts => {
      document.getElementById("forks-text").innerHTML = txts[0][2]['forks_count']; // forks of repo
      document.getElementById("watchers-text").innerHTML = txts[0][2]['watchers_count']; // watchers of repo
      document.getElementById("commits-text").innerHTML = txts[0][2]['open_issues']; // commits of repo
    });
}


function addEventListeners() {
  exitContactDialog.addEventListener("click", contactDialogExit);
  openContactDialog.addEventListener("click", contactDialogDisplay);
  exitButton.addEventListener("click", dialogExit);
  subscribeBtn.addEventListener("click", subscribeDialogDisplay);
  menuToggle.addEventListener("click", toggleMenu);
  contactForm.addEventListener('submit', contactFormSubmission);
  document.addEventListener('DOMContentLoaded', typeWriter);
  document.addEventListener('DOMContentLoaded', fetchJsonApi);
  document.addEventListener('DOMContentLoaded', gallery);
  document.addEventListener('DOMContentLoaded', menuNavMobileDisplay);
  document.addEventListener('DOMContentLoaded', playVideo);
  document.addEventListener('DOMContentLoaded', menuBarLinksActive);
}

addEventListeners();
