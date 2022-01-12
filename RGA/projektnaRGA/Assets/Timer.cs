using System.Collections;
using System.Collections.Generic;
using UnityEngine.UI;
using UnityEngine;

public class Timer : MonoBehaviour
{
    float timerF = 0f;
    public GameObject canvas;
    private Text timer;
    // Start is called before the first frame update
    void Start()
    {
        timer = canvas.AddComponent<Text>();
        Font font = (Font)Resources.GetBuiltinResource(typeof(Font), "Arial.ttf");
        timer.font = font;
        timer.fontSize = 50;
        timer.alignment = TextAnchor.UpperCenter;

    }

    // Update is called once per frame
    void Update()
    {
        timerF += Time.deltaTime;

        int min = (int)(Mathf.Round(timerF) / 60);
        int sec = (int)(Mathf.Round(timerF) % 60);

        timer.text = string.Format("\n\n\n{0:00}:{1:00}", min, sec);


    }
}
