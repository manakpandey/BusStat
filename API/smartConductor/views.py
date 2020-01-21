from django.shortcuts import render, redirect, HttpResponse
from smartConductor.forms import TokenForm
import requests


def verify_token(request):
    if request.method == 'POST':
        form = TokenForm(request.POST)
        if form.is_valid():
            token = form.cleaned_data['token']
            url = "http://192.168.43.21:8000/token/verify/" + token + "/"
            r = requests.get(url)
            data = r.json()
            if data["status"] != "invalid":
                context = {'msg': 'Token verified for ' + data['username']}
                return render(request, 'result.html', context=context)
            else:
                context = {'msg': 'Invalid Token'}
                return render(request, 'result.html', context=context)

    else:
        form = TokenForm()
        context = {'form': form}
        return render(request, 'verify_token.html', context=context)
